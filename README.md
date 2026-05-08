# DooRDash University Project

Java university project developed in milestone-based stages, with AI-assisted iteration during development.

## Overview

DooRDash is a turn-based game engine project.  
The repository is organized by milestones so each folder represents a specific stage of progress.

## Repository Structure

```text
DooRDash-UniProj-/
├── DooRDasH/      # Milestone 1
├── DoorDashM2/    # Milestone 2
└── README.md
```

Each milestone folder contains:
- `src/` (Java source code)
- `cards.csv`
- `cells.csv`
- `monsters.csv`

## Tech Stack

- Java
- CSV-based game data loading

## Getting Started

### Prerequisites

- JDK 8+ (or newer)
- Any Java IDE (IntelliJ IDEA, Eclipse, VS Code) or command line

### Compile engine sources (command line)

From the repository root:

```bash
# Milestone 1 engine sources
find DooRDasH/src/game/engine -name "*.java" | sort > /tmp/m1_sources.txt
javac -d /tmp/m1_out @/tmp/m1_sources.txt

# Milestone 2 engine sources
find DoorDashM2/src/game/engine -name "*.java" | sort > /tmp/m2_sources.txt
javac -d /tmp/m2_out @/tmp/m2_sources.txt
```

These commands compile the core engine packages only.  
Test files under `src/game/tests` can be run from an IDE with the required JUnit setup.

## Milestones

- **Milestone 1 (`DooRDasH`)**: core game implementation
- **Milestone 2 (`DoorDashM2`)**: expanded engine and gameplay elements
- **Milestone 3**: planned

## Notes

- This is an academic project.
- Development was done by the project owner with AI assistance for ideation, debugging, and refinement.
- AI tools used during development included GPT models, GLM-5, Kimi K2.5, and Minimax m2.7.
