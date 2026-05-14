# IDE-Project
# Network Intrusion Detection System
A console-based Java simulation of a real-time network intrusion detection system. The program generates randomized network traffic, processes it through a live log feed, and fires alerts when attack patterns are detected. Built for JGrasp and GitHub Codespaces — no external libraries required.

---

## How to Run

**In GitHub Codespaces or any terminal with Java installed:**
```
javac ids/src/ids/*.java
java -cp ids/src ids.Main
```

---

## Features

- **Live Log Feed** — simulates real network traffic with randomized IP addresses, ports, protocols, event types, and severity levels
- **Brute Force Detection** — fires an alert when one IP accumulates 5 or more failed login attempts
- **Port Scan Detection** — fires an alert when one IP probes 6 or more distinct ports
- **Rule Toggle System** — enable or disable detection rules individually using a looping configuration menu; changes take effect on the next simulation run
- **Simulation Report** — displays a full summary of IP event counts, login failures, ports probed, and all alerts fired
- **Input Validation** — all user inputs are validated; the program never crashes on bad input

---

## Data Structures Used

| Structure | Where | Purpose |
|---|---|---|
| `Queue` (LinkedList) | LogProcessor | FIFO processing of log entries |
| `HashMap` | LogProcessor | Tracks total events per IP address |
| `Stack` | AlertManager | Stores active detection rules |
| `ArrayList` | AlertManager | Stores all fired alerts |
| `HashSet` | DetectionEngine | Tracks distinct ports per IP |

---

## File Structure

```
ids/src/ids/
├── LogEntry.java         — stores the 5 aspects of a network event
├── LogGenerator.java     — generates randomized log entries
├── LogProcessor.java     — Queue + HashMap processing
├── AlertManager.java     — Stack rule management + ArrayList alert log
├── DetectionEngine.java  — brute force and port scan detection logic
└── Main.java             — console UI and main menu
```

---

## Menu Options

```
1. Configure rules   — enable/disable detection rules without leaving the menu
2. Run simulation    — generate and process a chosen number of log entries
3. Generate report   — display full summary of the last simulation
4. Exit
```

---

## Example Output

```
  [2026-05-14T00:12:32.880Z] 45.33.32.156 | PORT_SCAN | port 161 | HIGH
  [2026-05-14T00:12:36.071Z] 203.0.113.42 | FAILED_LOGIN | port 22 | MEDIUM
  *** ALERT: BRUTE FORCE -- IP: 203.0.113.42 | Failed logins: 5
  [Alerts so far: 1]
  [2026-05-14T00:12:40.633Z] 192.168.1.10 | PORT_SCAN | port 443 | LOW
  *** ALERT: PORT SCAN -- IP: 192.168.1.10 | Distinct ports: 6
  [Alerts so far: 2]
```

---

## Arqaa
Built as a year-end major project for Computer Science III