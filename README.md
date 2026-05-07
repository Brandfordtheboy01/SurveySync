# Customer Questionnaire Application

A Mendix low-code application that allows customers to fill out a structured survey and enables managers to view, filter, and analyse all collected responses through a summary dashboard.

---

## Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Roles & Security](#roles--security)
- [Technology Stack](#technology-stack)
- [Prerequisites](#prerequisites)
- [Getting Started](#getting-started)
- [Project Structure](#project-structure)
- [Questionnaire Design](#questionnaire-design)
- [Draft & Resume Workflow](#draft--resume-workflow)
- [Statistics & Summary Calculation](#statistics--summary-calculation)
- [Email Notifications](#email-notifications)
- [Manager Dashboard](#manager-dashboard)
- [Localisation](#localisation)
- [Responsive Design](#responsive-design)
- [Testing](#testing)
- [Deployment](#deployment)
- [Contributing](#contributing)
- [License](#license)

---

## Overview

This application provides a simple customer questionnaire tool built on the **Mendix** platform. Users can start a survey, save their progress as a draft, resume at any time, and submit when ready. Upon submission, summary statistics are automatically calculated and the Manager receives an email notification with a direct link to the results dashboard.

---

## Features

| Feature                | Description                                                                                                |
| ---------------------- | ---------------------------------------------------------------------------------------------------------- |
| **10-Question Survey** | Mixed question types: open text, single-choice (radio), multiple-choice (checkbox), and 1–5 rating scale   |
| **Draft & Resume**     | Users can save progress and return later to complete the questionnaire                                     |
| **Auto Statistics**    | On submission: response count, average ratings, and overall completion rate are recalculated automatically |
| **Email Notification** | Manager receives an automated email with key stats and a direct link to the summary page                   |
| **Manager Dashboard**  | List and summary views with search/filter by date or user                                                  |
| **Charts**             | Bar and pie charts using the Mendix Charts module                                                          |
| **Localisation**       | Full UI translation in English + German (or another language) via Mendix built-in translation editor       |
| **Responsive Design**  | All pages adapt to desktop, tablet, and mobile viewports                                                   |

---

## Roles & Security

| Role        | Permissions                                                                                   |
| ----------- | --------------------------------------------------------------------------------------------- |
| **User**    | Create and read their own questionnaire responses only. No access to other users' data.       |
| **Manager** | Full read access to all responses, summaries, and the dashboard. No edit rights on responses. |

Security is enforced through Mendix module roles and entity-level access rules configured in the domain model.

---

## Technology Stack

- **Platform:** Mendix Studio Pro (version X.X or higher recommended)
- **Charts:** Mendix Charts module (from the Mendix Marketplace)
- **Email:** Mendix Email Connector or Community Commons `sendEmail` action
- **Localisation:** Mendix built-in Batch Translate / Translation Editor
- **UI Framework:** Mendix Atlas UI (responsive out of the box)

---

## Prerequisites

- Mendix Studio Pro installed ([download here](https://marketplace.mendix.com/link/studiopro/))
- A Mendix Team Server account (or local deployment)
- SMTP credentials configured for email notifications (see [Email Notifications](#email-notifications))
- The following Marketplace modules imported into the project:
  - **Charts** (`mx-charts`)
  - **Email Connector** (or equivalent)

---

## Getting Started

1. **Clone / check out the repository**

   ```bash
   git clone https://your-repo-url/customer-questionnaire.git
   ```

2. **Open the project in Mendix Studio Pro**

   File → Open Project → select the `.mpr` file.

3. **Import required Marketplace modules**

   App → Marketplace → search for _Charts_ and _Email Connector_ → Import.

4. **Configure SMTP settings**

   Navigate to `App Settings → Configurations → Constants` and fill in:

   | Constant                      | Example Value         |
   | ----------------------------- | --------------------- |
   | `EmailConnector.SMTPHost`     | `smtp.example.com`    |
   | `EmailConnector.SMTPPort`     | `587`                 |
   | `EmailConnector.SMTPUsername` | `noreply@example.com` |
   | `EmailConnector.SMTPPassword` | `••••••••`            |
   | `App.ManagerEmailAddress`     | `manager@example.com` |

5. **Run locally**

   Click **Run Locally** (F5) in Studio Pro. The app will open in your default browser at `http://localhost:8080`.

6. **Create test accounts**

   Use the default administrator account to create at least one _User_ and one _Manager_ account and assign the appropriate module roles.

---

## Project Structure

```
CustomerQuestionnaire/
├── domain-model/
│   ├── Questionnaire          # Master questionnaire definition
│   ├── Question               # Individual question records (type, order, label)
│   ├── Response               # One record per user submission (or draft)
│   ├── Answer                 # Linked to Response; stores value per question
│   └── SummaryStatistics      # Calculated aggregate entity
├── microflows/
│   ├── ACT_Response_SaveDraft
│   ├── ACT_Response_Submit
│   ├── ACT_Statistics_Recalculate
│   └── ACT_Email_NotifyManager
├── pages/
│   ├── User/
│   │   ├── Questionnaire_Start
│   │   ├── Questionnaire_Fill  (draft + submit actions)
│   │   └── Questionnaire_ThankYou
│   └── Manager/
│       ├── Dashboard_Overview
│       ├── Response_List       (search / filter)
│       └── Response_Detail
├── resources/
│   └── EmailTemplates/
│       └── ManagerNotification.html
└── i18n/
    ├── en_US.lang
    └── de_DE.lang              (or chosen second language)
```

---

## Questionnaire Design

The survey contains **10 predefined questions** covering a range of question types:

| #   | Question (English)                        | Type                       |
| --- | ----------------------------------------- | -------------------------- |
| 1   | What is your full name?                   | Open Text                  |
| 2   | How did you hear about us?                | Single Choice (Radio)      |
| 3   | Which products have you used?             | Multiple Choice (Checkbox) |
| 4   | Rate your overall satisfaction (1–5)      | Rating Scale               |
| 5   | Rate the quality of our support (1–5)     | Rating Scale               |
| 6   | Would you recommend us to a friend?       | Single Choice (Radio)      |
| 7   | Which features do you find most valuable? | Multiple Choice (Checkbox) |
| 8   | Rate the ease of use of our product (1–5) | Rating Scale               |
| 9   | What could we improve?                    | Open Text                  |
| 10  | Any additional comments?                  | Open Text                  |

Questions are stored as data records in the `Question` entity, making them manageable without code changes.

---

## Draft & Resume Workflow

1. A `Response` record is created with status `Draft` when the user starts the questionnaire.
2. `ACT_Response_SaveDraft` persists the current `Answer` objects without changing the status.
3. On the User's home page, any existing `Draft` response is surfaced with a **Resume** button, which re-opens the questionnaire at the last saved state.
4. `ACT_Response_Submit` sets the status to `Submitted`, records the submission timestamp, triggers statistics recalculation, and fires the manager email notification.

---

## Statistics & Summary Calculation

`ACT_Statistics_Recalculate` runs after every submission and updates the `SummaryStatistics` entity with:

- **Total response count** — count of all `Submitted` responses.
- **Average rating per question** — mean of all numeric answers for each rating-scale question.
- **Overall completion rate** — percentage of started questionnaires that reached `Submitted` status.

The microflow uses Mendix aggregate functions (`Average`, `Count`) over the `Answer` association, keeping calculations in-platform without custom Java.

---

## Email Notifications

Upon submission, `ACT_Email_NotifyManager` sends an HTML email to the configured manager address containing:

- Submission timestamp and the submitting user's name.
- Snapshot of key statistics (total responses, average ratings).
- A deep link to the `Dashboard_Overview` page.

The email template is stored in `resources/EmailTemplates/ManagerNotification.html` and rendered via the Email Connector's template engine.

---

## Manager Dashboard

### Response List (`Manager/Response_List`)

- Displays all `Submitted` responses in a data grid.
- **Search / Filter** options:
  - Date range picker (submission date).
  - User name text search.
- Clicking a row opens `Response_Detail` for the full answer breakdown.

### Summary Dashboard (`Manager/Dashboard_Overview`)

- **KPI tiles:** Total Responses, Average Overall Rating, Completion Rate.
- **Bar chart:** Average rating per rating-scale question.
- **Pie chart:** Distribution of single-choice answers for a selected question.
- Charts are rendered using the Mendix **Charts** module (AnyChart-based).

---

## Localisation

The application is fully localised in **English (en_US)** and **German (de_DE)**.

To add or update translations:

1. In Studio Pro, open **App → Tools → Batch Translate**.
2. Select the target language.
3. Translate all listed texts and save.
4. Alternatively, export the language file, edit externally, and re-import via **App → Tools → Language Operations → Import**.

To add a new language:

1. **App → Project Settings → Languages** → Add.
2. Run Batch Translate for the new language.

---

## Responsive Design

All pages are built on the **Mendix Atlas UI** design system which uses a 12-column grid layout. Responsive behaviour is achieved through:

- Layout containers with breakpoint-aware column settings (desktop / tablet / phone).
- The `phone-full-width` and `tablet-full-width` CSS helpers applied where needed.
- No fixed pixel widths on page content areas.

Pages tested at the following breakpoints:

| Breakpoint | Width           |
| ---------- | --------------- |
| Desktop    | ≥ 992 px        |
| Tablet     | 768 px – 991 px |
| Mobile     | < 768 px        |

---

## Testing

### Manual Testing Checklist

- [ ] User can start a questionnaire and all 10 questions render correctly.
- [ ] All question types (text, radio, checkbox, rating) accept and save input.
- [ ] Draft save persists answers after browser refresh.
- [ ] Resume correctly restores previously saved answers.
- [ ] Submission changes status to `Submitted` and prevents re-submission.
- [ ] Statistics recalculate correctly after each new submission.
- [ ] Manager email is received with correct stats and working deep link.
- [ ] Manager can filter response list by date range and user name.
- [ ] Charts render on the dashboard with real data.
- [ ] UI is usable on mobile viewport (375 px width).
- [ ] All visible UI text is translated in the second language when locale is switched.
- [ ] A User account cannot access the Manager pages (returns 403 / redirect).
- [ ] A Manager account cannot see another user's draft responses.

---

## Deployment

1. In Studio Pro, select **App → Deploy to Mendix Cloud** (or your target environment).
2. Ensure all environment-specific constants (SMTP, Manager email) are set in the **Environment Details** page of the Mendix Developer Portal.
3. After deployment, run the **Database Synchronisation** if the domain model has changed.
4. Verify the app URL and test the Manager deep-link in the notification email.

---

## Contributing

1. Create a feature branch from `main`: `git checkout -b feature/your-feature-name`.
2. Make changes in Studio Pro and commit the `.mpr` file along with any resource changes.
3. Open a Pull Request and assign at least one reviewer.
4. Merge only after passing the manual testing checklist above.

---

## License

This project is proprietary. All rights reserved. Redistribution or use outside the scope of the assigned project is not permitted without explicit written consent.
