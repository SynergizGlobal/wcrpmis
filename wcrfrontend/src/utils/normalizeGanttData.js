// src/utils/normalizeGanttData.js

/**
 * Normalize input data for Gantt chart.
 * Handles both Excel-style arrays and DB JSON array.
 * Parses subStructure "from xx km to yy km" to numeric values.
 */
export function normalizeGanttInput(input) {
  if (!input) return [];

  // Excel-style (fallback)
  if (Array.isArray(input) && Array.isArray(input[0])) {
    return input
      .slice(1)
      .filter((row) => row && row.length >= 9)
      .map((row) => ({
        contract: row[2],
        contractor: row[3],
        structureType: row[4] || "",
        subStructure: row[5] || "",
        fromKm: Number(row[6]),
        toKm: Number(row[7]),
        status: row[8],
        progress: 0,
        barColor: "blue",
        barLabel: "",
        subFromKm: Number(row[6]),
        subToKm: Number(row[7]),
      }))
      .filter((r) => !Number.isNaN(r.fromKm) && !Number.isNaN(r.toKm));
  }

  // DB JSON array
  if (Array.isArray(input)) {
    return input
      .map((r) => {
        let subFromKm = Number(r.fromKm);
        let subToKm = r.toKm != null ? Number(r.toKm) : Number(r.fromKm);

        // Parse subStructure "from xx km to yy km"
        if (r.subStructure) {
          const match = r.subStructure.match(/from\s+([\d.]+)\s*km\s+to\s+([\d.]+)\s*km/i);
          if (match) {
            subFromKm = parseFloat(match[1]);
            subToKm = parseFloat(match[2]);
          }
        }

        return {
          contract: r.contract ?? "",
		  contract_id: r.contract_id ?? "",
          contractor: r.contractor ?? "",
          structureType: r.structureType ?? "",
          structure: r.structure ?? "",
          subStructure: r.subStructure ?? "",
          projectFromKm: r.projectFromKm != null ? Number(r.projectFromKm) : undefined,
          projectToKm: r.projectToKm != null ? Number(r.projectToKm) : undefined,
          fromKm: Number(r.fromKm),
          toKm: r.toKm == null ? Number(r.fromKm) : Number(r.toKm),
          subFromKm,  // numeric start for bar
          subToKm,    // numeric end for bar
          status: r.status ?? "Contract Awarded",
          progress: r.progress != null ? Number(r.progress) : 0,
          barColor: r.barColor ?? "",
          barLabel: r.barLabel ?? "",
        };
      })
      .filter((r) => !Number.isNaN(r.fromKm) && !Number.isNaN(r.toKm));
  }

  return [];
}

/**
 * Convert status and progress to CSS class for bar color.
 */
export function statusToClass(status, progress) {
  const s = (status || "").toLowerCase();
  const p = Number(progress);

  if (s.includes("not awarded")) return "not-awarded";
  if (p === 100) return "completed";                 // Green
  if (p >= 90 && p < 100) return "almost-completed"; // Light Green
  if (p > 0 && p < 90) return "in-progress";        // Orange
  if (p === 0) return "not-started";                // Blue
  if (s.includes("in progress") || s.includes("progress")) return "in-progress";

  return "not-awarded"; // fallback
}

/**
 * Assign non-overlapping layers to segments so stacked bars don't collide.
 */
export function layerSegments(segments) {
  const layers = [];
  const sorted = segments
    .slice()
    .sort((a, b) =>
      a.subFromKm !== b.subFromKm
        ? a.subFromKm - b.subFromKm
        : (b.subToKm - b.subFromKm) - (a.subToKm - a.subFromKm)
    );

  sorted.forEach((seg) => {
    let idx = 0;
    while (true) {
      if (!layers[idx]) layers[idx] = [];
      const overlap = layers[idx].some(
        (s) => !(seg.subToKm <= s.subFromKm || seg.subFromKm >= s.subToKm)
      );
      if (!overlap) {
        seg.layer = idx;
        layers[idx].push(seg);
        break;
      }
      idx += 1;
    }
  });

  return sorted;
}
