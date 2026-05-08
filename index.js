//  PASSWORD VALIDATION
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

  form.addEventListener("submit", function (e) {
    const password = passwordInput.value;

    if (!validatePassword(password)) {
      e.preventDefault();

      errorText.textContent =
        "Password must be 8–16 chars and include letters, numbers, and symbols.";
    } else {
      errorText.textContent = "";
    }
  });


  // USERNAME VALIDATION
  const usernameInput = document.getElementById("username");
const usernameError = document.querySelector(".username-error");


// USERNAME VALIDATION ONLY

function validateUsername() {

  const usernameValue = usernameInput.value.trim();

  // Check if empty

  if (usernameValue === "") {

    usernameError.textContent =
      "Please enter a username.";

    return false;
  }

  // Check if only alphabets

  if (!/^[A-Za-z]+$/.test(usernameValue)) {

    usernameError.textContent =
      "Username must contain alphabets only.";

    return false;
  }

  // If valid

  usernameError.textContent = "";

  return true;
}

form.addEventListener("submit", function (e) {

  const isUsernameValid = validateUsername();
  const isPasswordValid = validatePassword(passwordInput.value);

  if (!isUsernameValid || !isPasswordValid) {
    e.preventDefault();
  }

});