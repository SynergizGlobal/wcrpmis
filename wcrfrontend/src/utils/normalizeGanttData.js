

export function normalizeGanttInput(input) {
  if (!input) return [];

  // Excel-style (first row is header, second row is data)
  if (Array.isArray(input) && Array.isArray(input[0])) {
    // Expecting columns like in your HTML EXCEL_DATA
    // ['S. No.', 'Project', 'Contract', 'Contractor', 'Structure', 'Sub-Str', 'From Chainage', 'To Chainage', 'Status']
    return input
      .slice(1) // skip header
      .filter((row) => row && row.length >= 9)
      .map((row) => ({
        contract: row[2],
        contractor: row[3],
        structure: row[4],
        subStructure: row[5] || "",
        fromKm: Number(row[6]),
        toKm: Number(row[7]),
        status: row[8],
      }))
      .filter((r) => !Number.isNaN(r.fromKm) && !Number.isNaN(r.toKm));
  }

  // DB JSON array (assume keys already meaningful)
  if (Array.isArray(input)) {
    return input
      .map((r, i) => ({
        contract: r.contract ?? "",
        contractor: r.contractor ?? "",
        structure: r.structure ?? "",
        subStructure: r.subStructure ?? "",
        fromKm: Number(r.fromKm),
        toKm: r.toKm == null ? Number(r.fromKm) : Number(r.toKm),
        status: r.status ?? "Contract Awarded",
      }))
      .filter((r) => !Number.isNaN(r.fromKm) && !Number.isNaN(r.toKm));
  }

  return [];
}

export function statusToClass(status) {
  if (!status) return "contract-awarded";
  const t = String(status).trim();
  const l = t.toLowerCase();

  if (t === "Completed" || l === "completed") return "completed";
  if (t === "Contract Awarded" || l === "contract awarded") return "contract-awarded";
  if (t === "Tender Invited" || l === "tender invited") return "tender-invited";
  if (t === "Tender to be Invited" || t === "Tender To Be Invited" || l === "tender to be invited")
    return "tender-to-be-invited";

  if (l.includes("completed")) return "completed";
  if (l.includes("awarded") && !l.includes("to be")) return "contract-awarded";
  if (l.includes("invited") && !l.includes("to be")) return "tender-invited";
  if (l.includes("to be invited")) return "tender-to-be-invited";

  return "contract-awarded";
}

/**
 * Assign non-overlapping layers to segments so stacked bars don't collide.
 */
export function layerSegments(segments) {
  const layers = [];
  const sorted = segments
    .slice()
    .sort((a, b) => (a.fromKm !== b.fromKm ? a.fromKm - b.fromKm : (b.toKm - b.fromKm) - (a.toKm - a.fromKm)));

  sorted.forEach((seg) => {
    let idx = 0;
    while (true) {
      if (!layers[idx]) layers[idx] = [];
      const overlap = layers[idx].some(
        (s) => !(seg.toKm <= s.fromKm || seg.fromKm >= s.toKm)
      );
      if (!overlap) {
        seg.layer = idx;
        layers[idx].push(seg);
        break;
      }
      idx += 1;
    }
  });

  // reverse so longer are visually "lower"
  const maxLayer = Math.max(...sorted.map((s) => s.layer));
  sorted.forEach((s) => (s.layer = maxLayer - s.layer));

  return sorted;
}
