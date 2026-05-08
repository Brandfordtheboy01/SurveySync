// PASSWORD VALIDATION
const passwordInput = document.getElementById("password");
const form = document.querySelector(".form-area");
const errorText = document.querySelector(".password-error");

function validatePassword(password) {
  const minLength = password.length >= 8;
  const maxLength = password.length <= 16;

  const hasLetter = /[a-zA-Z]/.test(password);
  const hasNumber = /\d/.test(password);
  const hasSymbol = /[!@#$%^&*(),.?":{}|<>]/.test(password);

  return minLength && maxLength && hasLetter && hasNumber && hasSymbol;
}

// USERNAME VALIDATION
const usernameInput = document.getElementById("username");
const usernameError = document.querySelector(".username-error");

function validateUsername() {
  const usernameValue = usernameInput.value.trim();

  if (usernameValue === "") {
    usernameError.textContent = "Please enter a username.";
    return false;
  }

  if (!/^[A-Za-z]+$/.test(usernameValue)) {
    usernameError.textContent = "Username must contain alphabets only.";
    return false;
  }

  usernameError.textContent = "";
  return true;
}

// ── ONE submit listener — both checks inside ───────────
form.addEventListener("submit", function (e) {
  const isUsernameValid = validateUsername();
  const isPasswordValid = validatePassword(passwordInput.value);

  if (!isUsernameValid || !isPasswordValid) {
    e.preventDefault();

    // show password error message if password failed
    if (!isPasswordValid) {
      errorText.textContent =
        "Password must be 8–16 chars and include letters, numbers, and symbols.";
    } else {
      errorText.textContent = "";
    }
  }
});