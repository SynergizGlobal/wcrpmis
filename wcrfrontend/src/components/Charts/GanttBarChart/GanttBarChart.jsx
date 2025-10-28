import React, { useMemo, useState } from "react";
import { Outlet } from 'react-router-dom';
import styles from './GanttBarChart.css';

import {
  normalizeGanttInput,
  statusToClass,
  layerSegments,
} from "../../../utils/normalizeGanttData";

function groupByStructure(rows) {
  const map = new Map();
  rows.forEach((r) => {
    if (!map.has(r.structure)) map.set(r.structure, []);
    map.get(r.structure).push(r);
  });
  return map;
}

function calcBounds(rows) {
  const nums = [];
  rows.forEach((r) => {
    if (typeof r.fromKm === "number") nums.push(r.fromKm);
    if (typeof r.toKm === "number") nums.push(r.toKm);
  });
  if (!nums.length) return { minKm: 0, maxKm: 100, totalRange: 100 };
  let min = Math.min(...nums);
  let max = Math.max(...nums);
  // pad to neat 10s with 5 buffer
  const minKm = Math.floor(min / 10) * 10 - 5;
  const maxKm = Math.ceil(max / 10) * 10 + 5;
  const totalRange = maxKm - minKm;
  return { minKm, maxKm, totalRange };
}

function posPct(km, minKm, totalRange) {
  return ((km - minKm) / totalRange) * 100;
}
function widthPct(fromKm, toKm, totalRange) {
  return ((toKm - fromKm) / totalRange) * 100;
}

function Legend() {
  return (
    <div className="g-legend">
      <div className="g-legend-item">
        <div className="g-legend-color completed" />
        <span>COMPLETED</span>
      </div>
      <div className="g-legend-item">
        <div className="g-legend-color contract-awarded" />
        <span>CONTRACT AWARDED</span>
      </div>
      <div className="g-legend-item">
        <div className="g-legend-color tender-invited" />
        <span>TENDER INVITED</span>
      </div>
      <div className="g-legend-item">
        <div className="g-legend-color tender-to-be-invited" />
        <span>TENDER TO BE INVITED</span>
      </div>
    </div>
  );
}

export default function GanttBarChart({
  title = "AGENCY POSITION",
  subtitle = "",
  excelData,             // Excel-style or DB-style array (either works)
  dateText,              // optional override
}) {
  const rows = useMemo(() => normalizeGanttInput(excelData), [excelData]);
  const { minKm, maxKm, totalRange } = useMemo(() => calcBounds(rows), [rows]);

  // Build projectData like your HTML script
  const projectData = useMemo(() => {
    const grouped = groupByStructure(rows);
    let sn = 1;
    const result = [];

    grouped.forEach((segments, structure) => {
      const isWelding = (structure || "").toLowerCase().includes("welding");

      // decorate with status class, length
      const segs = segments.map((s) => ({
        ...s,
        statusClass: statusToClass(s.status),
        length: Math.abs((s.toKm ?? s.fromKm) - s.fromKm),
      }));

      if (isWelding && segs.some((s) => s.subStructure)) {
        // group by sub-structure; stack groups
        const subGroups = new Map();
        segs.forEach((s) => {
          const key = s.subStructure || "Unknown";
          if (!subGroups.has(key)) subGroups.set(key, []);
          subGroups.get(key).push(s);
        });

        let stacked = [];
        let currentLayerOffset = 0;

        subGroups.forEach((items) => {
          const layered = layerSegments(items);
          const maxLayer = Math.max(...layered.map((x) => x.layer || 0));
          layered.forEach((x) => {
            x.layer = (x.layer || 0) + currentLayerOffset;
            x._subGroup = items[0].subStructure || "Unknown";
          });
          stacked = stacked.concat(layered);
          currentLayerOffset += maxLayer + 1;
        });

        result.push({
          sn: sn++,
          description: String(structure || "").toUpperCase(),
          segments: stacked,
          isWeldingWithSubTypes: true,
        });
      } else {
        result.push({
          sn: sn++,
          description: String(structure || "").toUpperCase(),
          segments: layerSegments(segs),
          isWeldingWithSubTypes: false,
        });
      }
    });

    return result;
  }, [rows]);

  const [tip, setTip] = useState(null);
  const today = useMemo(
    () => (dateText ? dateText : new Date().toLocaleDateString("en-GB")),
    [dateText]
  );

  const chainageMarks = [];
  for (let km = minKm; km <= maxKm; km += 5) {
    chainageMarks.push(
      <div key={km} className="g-chainage-mark">
        <div className="g-km-mark">{km}</div>
      </div>
    );
  }

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
            <div className="g-desc-head">DESCRIPTION</div>
            <div className="g-chainage-head">
              CHAINAGE (KM)
              <div className="g-chainage-numbers">{chainageMarks}</div>
            </div>
          </div>

          {/* rows */}
          {projectData.map((row) => {
            const maxLayers =
              Math.max(...row.segments.map((s) => s.layer || 0)) + 1;
            const requiredHeight = Math.max(60, 35 * maxLayers);

            // track displayed sub-structure labels per row
            const added = new Set();

            return (
              <div className="g-row" key={row.sn}>
                <div className="g-sn-cell" style={{ minHeight: requiredHeight }}>
                  {row.sn}
                </div>

                <div
                  className="g-desc-cell"
                  style={{ minHeight: requiredHeight }}
                >
                  {row.description}
                </div>

                <div
                  className="g-timeline"
                  style={{ minHeight: requiredHeight, position: "relative" }}
                >
                  {row.segments.map((seg, i) => {
                    const layer = seg.layer || 0;
                    const top = 5 + layer * 35;
                    const isPoint = seg.fromKm === seg.toKm;
                    const left = posPct(seg.fromKm, minKm, totalRange);
                    const width = isPoint
                      ? 0
                      : widthPct(seg.fromKm, seg.toKm, totalRange);

                    const chainageLength = Math.abs(seg.toKm - seg.fromKm);
                    const contractorName = String(seg.contractor || "")
                      .replace(/M\/s\s*/gi, "")
                      .trim();

                    const showName = !isPoint && chainageLength >= 5;

                    const segStyle = {
                      top,
                      left: `${left}%`,
                      width: isPoint ? 8 : `${width}%`,
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

                    const subLabelNeeded =
                      row.isWeldingWithSubTypes &&
                      seg.subStructure &&
                      !added.has(`${seg.subStructure}-${layer}`);

                    if (subLabelNeeded) {
                      added.add(`${seg.subStructure}-${layer}`);
                    }

                    return (
                      <React.Fragment key={i}>
                        {/* sub-structure label (once per layer-group) */}
                        {subLabelNeeded && (
                          <div
                            className="g-substructure-label"
                            style={{
                              left: `calc(${left}% - 80px)`,
                              top,
                            }}
                          >
                            {seg.subStructure}
                          </div>
                        )}

                        {/* bar */}
                        <div
                          className={`g-seg ${seg.statusClass} ${
                            isPoint ? "g-point" : ""
                          }`}
                          style={segStyle}
                          onMouseEnter={(e) => {
                            const pos = { x: e.clientX + 10, y: e.clientY - 10 };
                            const posText = isPoint
                              ? `KM ${seg.fromKm}`
                              : `KM ${seg.fromKm} - ${seg.toKm}`;
                            setTip({
                              x: pos.x,
                              y: pos.y,
                              html: `
                                <strong>Contract:</strong> ${seg.contract}<br/>
                                <strong>Contractor:</strong> ${seg.contractor}<br/>
                                <strong>Chainage:</strong> ${posText}<br/>
                                <strong>Status:</strong> ${seg.statusClass
                                  .replace("-", " ")
                                  .toUpperCase()}${
                                seg.subStructure
                                  ? `<br/><strong>Sub-Structure:</strong> ${seg.subStructure}`
                                  : ""
                              }`,
                            });
                          }}
                          onMouseMove={(e) =>
                            setTip((t) => (t ? { ...t, x: e.clientX + 10, y: e.clientY - 10 } : t))
                          }
                          onMouseLeave={() => setTip(null)}
                        />

                        {/* contractor name */}
                        <div className="g-name" style={nameStyle}>
                          {contractorName}
                        </div>
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

      {/* tooltip */}
      {tip && (
        <div
          className="g-tooltip"
          style={{ left: tip.x, top: tip.y }}
          dangerouslySetInnerHTML={{ __html: tip.html }}
        />
      )}
    </div>
  );
}