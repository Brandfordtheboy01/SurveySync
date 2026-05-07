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