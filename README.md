# AI-Powered Travel Planner

AI-Powered Travel Planner is an Android application built with **Jetpack Compose**, designed to help users plan trips intelligently. It generates personalized itinerary suggestions based on destination and trip duration, allowing travelers to efficiently plan their journeys.

---


## Table of Contents

- [Features](#features)
- [Architecture](#architecture)
- [Tech Stack](#tech-stack)
- [Contact](#contact)

---

## Features

- **Destination Search:** Enter a location and trip duration to receive curated itinerary suggestions. Search for additional itineraries beyond the initial suggestions.
- <img width="128" height="285" alt="Screenshot1" src="https://github.com/user-attachments/assets/e49348f5-79d5-4a9f-8ff7-fdf21b7e7655" />
  <img width="128" height="285" alt="Screenshot3" src="https://github.com/user-attachments/assets/922a0f5a-8712-42da-91e5-6b1b2145f251" />
  <img width="128" height="285" alt="Screenshot4" src="https://github.com/user-attachments/assets/a26767a6-15c7-4242-91d9-98c5b152bdb2" />
  <img width="128" height="285" alt="Screenshot8" src="https://github.com/user-attachments/assets/028e014f-41ee-4eb6-946f-1f94edf8b8e9" />

- **Itinerary Details:** Explore detailed plans for each suggested trip.
- <img width="128" height="285" alt="Screenshot7" src="https://github.com/user-attachments/assets/bedbc3be-3d2a-4461-8c9c-146174d1e5be" />
  <img width="128" height="285" alt="Screenshot10" src="https://github.com/user-attachments/assets/4a3c5054-e9bc-48a1-9583-b783dedfac9b" />

- **Favorites:** Save itineraries to favorites list for quick access.
- <img width="128" height="285" alt="Screenshot5" src="https://github.com/user-attachments/assets/f0a28be8-af45-497f-bed7-9ff4901b80a9" />
  <img width="128" height="285" alt="Screenshot11" src="https://github.com/user-attachments/assets/6930add0-aea9-4869-8c2b-076d417112d0" />

---

## Architecture

The app follows a **Clean MVVM architecture** with a modular structure, combining **Clean Code principles** and a clear separation of concerns.

### Layers Overview

1. **Data Layer**
   - Handles all data sources: remote API calls and local persistence (DataStore/Room).
   - Components:
     - `model` – data representations from API or database.
     - `mapper` – transforms data between layers.
     - `remote` – Retrofit API services.
     - `repository` – concrete implementation of data operations.
     - `di` – dependency injection setup for data components.

2. **Domain Layer**
   - Core business logic and abstractions.
   - Components:
     - `interactor` – use cases for application logic.
     - `model` – domain-specific entities.
     - `repository` – interface definitions for repositories.

3. **Presentation Layer**
   - Manages UI-related state and logic.
   - Components:
     - `ViewModel` – exposes reactive state for the UI.
     - `ui` – Composable screens observing ViewModel state.

### Key Principles

- **Separation of Concerns:** Data, domain, and presentation are clearly separated.  
- **Unidirectional Data Flow:** Data flows from Data → Domain → ViewModel → UI.  
- **Clean Code:** Modular, testable, maintainable, and readable.  
- **Dependency Injection:** All dependencies are injected to improve scalability and testability.  

---

## Tech Stack

- **Kotlin** – main programming language
- **Jetpack Compose** – UI toolkit
- **Retrofit** – for network requests (Gemini API integration)
- **Kotlin Coroutines** – asynchronous operations
- **Hilt** - dependency injection
- **Android Studio & Gradle** – project setup and build management
- **Custom Theme** – modern and consistent UI
- **Modern UI:** Fully customized theme built with Jetpack Compose.
---

## Contact
**Anca Maria Mischie**
- **GitHub**: https://github.com/ancamariamischie
- **Email**: ancamariamischie@gmail.com
