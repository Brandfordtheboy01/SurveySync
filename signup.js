// ── Grab elements ──────────────────────────────────────
const form          = document.getElementById("signup-form");
const emailInput    = document.getElementById("email");
const usernameInput = document.getElementById("username");
const passwordInput = document.getElementById("password");
const emailError    = document.querySelector(".email-error");
const usernameError = document.querySelector(".username-error");
const passwordError = document.querySelector(".password-error");
const signUpBtn     = document.querySelector(".btn-signin");

// ── Validate email ─────────────────────────────────────
function validateEmail() {
  const value = emailInput.value.trim();

  if (value === "") {
    emailError.textContent = "Please enter your email.";
    return false;
  }
  if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(value)) {
    emailError.textContent = "Please enter a valid email address.";
    return false;
  }

  emailError.textContent = "";
  return true;
}

// ── Validate username ──────────────────────────────────
function validateUsername() {
  const value = usernameInput.value.trim();

  if (value === "") {
    usernameError.textContent = "Please enter a username.";
    return false;
  }
  if (!/^[A-Za-z]+$/.test(value)) {
    usernameError.textContent = "Username must contain letters only.";
    return false;
  }

  usernameError.textContent = "";
  return true;
}

// ── Validate password ──────────────────────────────────
function validatePassword() {
  const value = passwordInput.value;

  if (value === "") {
    passwordError.textContent = "Please enter a password.";
    return false;
  }
  if (value.length < 8 || value.length > 16) {
    passwordError.textContent = "Password must be 8–16 characters.";
    return false;
  }
  if (!/[a-zA-Z]/.test(value)) {
    passwordError.textContent = "Password must include at least one letter.";
    return false;
  }
  if (!/\d/.test(value)) {
    passwordError.textContent = "Password must include at least one number.";
    return false;
  }
  if (!/[!@#$%^&*(),.?":{}|<>]/.test(value)) {
    passwordError.textContent = "Password must include at least one symbol.";
    return false;
  }

  passwordError.textContent = "";
  return true;
}

// ── Clear errors while typing ──────────────────────────
emailInput.addEventListener("input", () => {
  emailError.textContent = "";
});
usernameInput.addEventListener("input", () => {
  usernameError.textContent = "";
});
passwordInput.addEventListener("input", () => {
  passwordError.textContent = "";
});

// ── ONE submit listener ────────────────────────────────
form.addEventListener("submit", function (e) {
  e.preventDefault();

  const isEmailValid    = validateEmail();
  const isUsernameValid = validateUsername();
  const isPasswordValid = validatePassword();

  if (!isEmailValid || !isUsernameValid || !isPasswordValid) return;

  // ── Loading state ────────────────────────────────────
  signUpBtn.textContent = "Creating account...";
  signUpBtn.disabled = true;

  // ── Mock signup (replace with real API call later) ───
  setTimeout(() => {
    // Save new user to session
    const newUser = {
      email:    emailInput.value.trim(),
      username: usernameInput.value.trim(),
      role:     "user",
    };
    sessionStorage.setItem("currentUser", JSON.stringify(newUser));

    // Redirect to home after successful signup
    window.location.href = "home.html";
  }, 800);
});