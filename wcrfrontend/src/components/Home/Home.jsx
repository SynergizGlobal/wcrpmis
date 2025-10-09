import React from "react";
import styles from "./Home.module.css";

export default function Home() {
  return (
    <div className={styles.home}>
      <div className={styles.stats}>
        <div className={styles.homeStatsCard}>
          <h3>6</h3>
          <p>Projects</p>
        </div>
        <div className={styles.homeStatsCard}>
          <h3>500.01 <span>km</span></h3>
          <p>Length</p>
        </div>
        <div className={styles.homeStatsCard}>
          <h3>20.01 <span>km</span></h3>
          <p>Commissioned Length</p>
        </div>
      </div>

      <div className={styles.buttons}>
        <button>NEW LINES</button>
        <button>DOUBLING</button>
        <button>GAUGE CONVERSION</button>
        <button>STATION WORKS</button>
        <button>CONTRACTORS ISSUES</button>
      </div>
    </div>
  );
}
