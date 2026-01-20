import React, { useMemo, useState } from "react";
import "./GanttBarChart.css";

import {
  normalizeGanttInput,
  statusToClass,
  layerSegments,
} from "../../../utils/normalizeGanttData";

// Group by structureType
function groupByStructure(rows) {
  const map = new Map();
  rows.forEach((r) => {
    if (!map.has(r.structureType)) map.set(r.structureType, []);
    map.get(r.structureType).push(r);
  });
  return map;
}

function calcBounds(rows) {
  if (!rows?.length) return { minKm: 0, maxKm: 100, totalRange: 100 };

  const projectFrom = Number(rows[0].projectFromKm ?? 0);
  const projectTo = Number(rows[0].projectToKm ?? 100);

  const minKm = projectFrom;
  const maxKm = projectTo;
  const totalRange = maxKm - minKm;

  return { minKm, maxKm, totalRange };
}

// Calculate position %
function posPct(km, minKm, totalRange) {
  const clamped = Math.max(minKm, Math.min(km, minKm + totalRange));
  return ((clamped - minKm) / totalRange) * 100;
}

// Calculate width %
function widthPct(fromKm, toKm, totalRange) {
  return ((toKm - fromKm) / totalRange) * 100;
}

// Legend component
function Legend() {
  return (
    <div className="g-legend">
      <div className="g-legend-item">
        <div className="g-legend-color completed" /> <span>Completed (100%)</span>
      </div>
      <div className="g-legend-item">
        <div className="g-legend-color almost-completed" />{" "}
        <span>Almost Completed (90–99%)</span>
      </div>
      <div className="g-legend-item">
        <div className="g-legend-color in-progress" /> <span>In Progress</span>
      </div>
      <div className="g-legend-item">
        <div className="g-legend-color not-started" /> <span>Not Started</span>
      </div>
      <div className="g-legend-item">
        <div className="g-legend-color not-awarded" /> <span>Not Awarded</span>
      </div>
    </div>
  );
}

export default function GanttBarChart({ title = "", subtitle = "", excelData, dateText, onSegmentClick    }) {
  const rows = useMemo(() => normalizeGanttInput(excelData), [excelData]);
  const { minKm, maxKm, totalRange } = useMemo(() => calcBounds(rows), [rows]);
  const HIDDEN_STRUCTURE_TYPES = [
    "LAND ACQUISITION",
    "UTILITY SHIFTING",
  ];

  // Group by structure type
  const projectData = useMemo(() => {
    const grouped = groupByStructure(rows);
    let sn = 1;
    const result = [];

    grouped.forEach((segments, structureType) => {
		const typeUpper = String(structureType || "").toUpperCase();

		if (HIDDEN_STRUCTURE_TYPES.includes(typeUpper)) {
		  return;
		}
      const segs = segments.map((s) => ({
        ...s,
        statusClass: statusToClass(s.status, s.progress),
        length: Math.abs((s.toKm ?? s.fromKm) - s.fromKm),
      }));

      result.push({
        sn: sn++,
        structureType: String(structureType || "").toUpperCase(),
        segments: layerSegments(segs),
      });
    });

    return result;
  }, [rows]);

  const [tip, setTip] = useState(null);
  const today = useMemo(() => (dateText ? dateText : new Date().toLocaleDateString("en-GB")), [dateText]);

  // Build X-axis chainage labels
  const interval = 25;
  const chainageValues = [];
  for (let km = minKm; km <= maxKm; km += interval) {
    chainageValues.push(Number(km.toFixed(2)));
  }
  if (chainageValues[chainageValues.length - 1] !== maxKm) chainageValues.push(maxKm);

  const chainageMarks = chainageValues.map((km) => (
    <div key={km} className="g-chainage-mark">
      <div className="g-km-mark">{km}</div>
    </div>
  ));

  return (
    <div className="g-container">
      <div className="g-title">{title}</div>
      {subtitle && <div className="g-subtitle">{subtitle}</div>}
      <div className="g-date">
        As on <span>{today}</span>
      </div>

      <div className="g-chart-outer">
        <div className="g-chart">
          <div className="g-header">
            <div className="g-sn-head">SN</div>
            <div className="g-desc-head">STRUCTURE TYPE</div>
            <div className="g-chainage-head">
              CHAINAGE (KM)
              <div className="g-chainage-numbers">{chainageMarks}</div>
            </div>
          </div>

          {projectData.map((row) => {
            const maxLayers = Math.max(...row.segments.map((s) => s.layer ?? 0)) + 1;
            const requiredHeight = Math.max(60, 35 * maxLayers);

            return (
              <div className="g-row" key={row.sn}>
                <div className="g-sn-cell" style={{ minHeight: requiredHeight }}>
                  {row.sn}
                </div>
                <div className="g-desc-cell" style={{ minHeight: requiredHeight }}>
                  {row.structureType}
                </div>

                <div className="g-timeline" style={{ minHeight: requiredHeight, position: "relative", width: "100%" }}>
                  {row.segments.map((seg, i) => {
                    const layer = seg.layer || 0;
                    const top = 5 + layer * 35;

                    // Use actual fromKm/toKm
                    const from = seg.fromKm;
                    const to = seg.toKm;
                    const isPoint = from === to;

                    const left = posPct(from, minKm, totalRange);
                    const width = widthPct(from, to, totalRange);

                    const chainageLength = Math.abs(to - from);
                    const contractorName = String(seg.contractor || "").replace(/M\/s\s*/gi, "").trim();
                    const showName = chainageLength >= 50;

                    const segStyle = {
                      position: "absolute",
                      top,
                      left: `${left}%`,
                      width: isPoint ? "8px" : `${width}%`,
                    };

                    const nameStyle = showName
                      ? {
                          left: `${left}%`,
                          top: top + 22,
                          width: `${width}%`,
                          textAlign: "center",
                          fontSize:
                            width * 14 < 60
                              ? "6px"
                              : width * 14 < 100
                              ? "7px"
                              : "8px",
                        }
                      : { display: "none" };

					  return (
					    <React.Fragment key={i}>
					      <div
					        className={`g-seg ${seg.statusClass} ${isPoint ? "g-point" : ""}`}
					        style={{ ...segStyle, cursor: "pointer" }}   // clickable cursor
					        onClick={() => {
					          if (typeof onSegmentClick === "function") {
					            onSegmentClick({
					              projectId: seg.projectId,
					              contract_id: seg.contract_id,
					              structureType: seg.structureType,
					              fromKm: seg.fromKm,
					              toKm: seg.toKm
					            });
					          }
					        }}
					        onMouseEnter={(e) => {
					          const pos = { x: e.clientX + 10, y: e.clientY - 10 };
					          setTip({
					            x: pos.x,
					            y: pos.y,
					            html: `
					              <strong>Structure Type:</strong> ${seg.structureType || ""}<br/>
					              <strong>Contract:</strong> ${seg.contract || ""}<br/>
					              <strong>Contractor:</strong> ${seg.contractor || ""}<br/>
					              <strong>Chainage:</strong> ${from} - ${to}<br/>
					              <strong>Status:</strong> ${seg.statusClass.replace("-", " ").toUpperCase()}<br/>
					              <strong>Progress:</strong> ${seg.progress ?? 0}%`,
					          });
					        }}
					        onMouseMove={(e) =>
					          setTip((t) => (t ? { ...t, x: e.clientX + 10, y: e.clientY - 10 } : t))
					        }
					        onMouseLeave={() => setTip(null)}
					      />
					      {showName && (
					        <div className="g-name" style={nameStyle}>
					          {contractorName}
					        </div>
					      )}
					    </React.Fragment>
					  );
                  })}
                </div>
              </div>
            );
          })}
        </div>
      </div>

      <Legend />

      {tip && (
        <div className="g-tooltip" style={{ left: tip.x, top: tip.y }} dangerouslySetInnerHTML={{ __html: tip.html }} />
      )}
    </div>
  );
}
