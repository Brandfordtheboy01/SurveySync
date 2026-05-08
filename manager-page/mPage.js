let surveyQuestions = [
  { id: 1, text: "Full Name", type: "text" },
  { id: 2, text: "Email Address", type: "text" },
  { id: 3, text: "Do you enjoy our platform?", type: "radio" },
  { id: 4, text: "Rate the UI design (1-5)", type: "rating" },
  { id: 5, text: "Primary Device used?", type: "text" },
  { id: 6, text: "Would you recommend us?", type: "radio" },
  { id: 7, text: "How did you find us?", type: "text" },
  { id: 8, text: "Rate site speed (1-5)", type: "rating" },
  { id: 9, text: "Feature most used?", type: "text" },
  { id: 10, text: "Final feedback", type: "text" }
];

let submissions = [];

document.addEventListener("DOMContentLoaded", () => {
  renderQuestionsList();
  attachSimulateButton();
});
const userViewBtn = document.querySelector(".btn-outline");

  userViewBtn.addEventListener("click", () => {
    window.location.href = "/customer-page/customer.html";
  });



function renderQuestionsList() {
  const container = document.getElementById("questionsList");
  const count = document.getElementById("activeQuestCount");


  container.innerHTML = "";

  surveyQuestions.forEach((q) => {
    const typeLabel =
      q.type === "text"
        ? "Open Text"
        : q.type === "radio"
        ? "Single Choice"
        : "Rating Scale";

    const card = document.createElement("div");
    card.className = "question-card";

    card.innerHTML = `
      <div class="question-card-info">
        <div class="question-card-text">${q.text}</div>
        <div class="question-card-type">Type: ${typeLabel}</div>
      </div>

      <div class="question-card-actions">
        <button class="btn-edit" data-id="${q.id}">Edit</button>
        <button class="btn-delete" data-id="${q.id}">Delete</button>
      </div>
    `;

    container.appendChild(card);
  });

  count.textContent = surveyQuestions.length;

  attachQuestionActions();
}


// =====================
// ADD QUESTION
// =====================
function addNewQuestion() {
  const text = document.getElementById("newQuestText").value;
  const type = document.getElementById("newQuestType").value;

  if (!text) {
    alert("Enter question text");
    return;
  }

  surveyQuestions.push({
    id: Date.now(),
    text,
    type
  });

  document.getElementById("newQuestText").value = "";

  renderQuestionsList();
}


// =====================
// DELETE QUESTION
// =====================
function deleteQuestion(id) {
  surveyQuestions = surveyQuestions.filter((q) => q.id !== id);
  renderQuestionsList();
}


// =====================
// EDIT QUESTION
// =====================
function editQuestion(id) {
  const question = surveyQuestions.find((q) => q.id === id);

  if (!question) return;

  const newText = prompt("Edit question:", question.text);

  if (newText) {
    question.text = newText;
    renderQuestionsList();
  }
}


// =====================
// BUTTON EVENTS (EDIT / DELETE)
// =====================
function attachQuestionActions() {
  const editButtons = document.querySelectorAll(".btn-edit");
  const deleteButtons = document.querySelectorAll(".btn-delete");

  editButtons.forEach((btn) => {
    btn.addEventListener("click", () => {
      editQuestion(Number(btn.dataset.id));
    });
  });

  deleteButtons.forEach((btn) => {
    btn.addEventListener("click", () => {
      deleteQuestion(Number(btn.dataset.id));
    });
  });
}


// =====================
// SUBMISSIONS (SIMULATION)
// =====================
function simulateSubmission() {
  submissions.push({
    id: submissions.length + 1,
    date: new Date().toLocaleTimeString()
  });

  renderSubmissions();
}

function renderSubmissions() {
  const list = document.getElementById("managerResponseList");

  list.innerHTML = submissions
    .map(
      (s) => `
      <li>
        <span>Submission #${s.id} - ${s.date}</span>
        <button class="btn-view">View</button>
      </li>
    `
    )
    .join("");
}


// =====================
// SIM BUTTON
// =====================
function attachSimulateButton() {
  const button = document.createElement("button");

  button.textContent = "Simulate New Submission";
  button.className = "btn-simulate";

  button.addEventListener("click", simulateSubmission);

  document
    .querySelector(".card:last-child")
    .appendChild(button);
}