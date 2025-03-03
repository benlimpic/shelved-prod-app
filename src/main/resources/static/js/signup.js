document.addEventListener("DOMContentLoaded", function () {
  //SIGNUP FORM ANIMATION
  const signupForm = document.querySelector("section");
  signupForm.style.opacity = 0;

  setTimeout(() => {
    signupForm.style.transition = "opacity 1s ease-in-out";
    signupForm.style.opacity = 1;
  }, 500);

  const signupButton = document.querySelector("button");
  signupButton.addEventListener("click", function () {
    const usernameInput = document.querySelector('input[type="text"]');
    const emailInput = document.querySelector('input[type="email"]');
    const passwordInput = document.querySelector('input[type="password"]');
    const confirmPasswordInput = document.querySelector(
      'input[type="password"][name="confirm-password"]'
    );

    // SIGNUP FORM VALIDATION

    const isValid =
      usernameInput.checkValidity() &&
      emailInput.checkValidity() &&
      passwordInput.checkValidity() &&
      confirmPasswordInput.checkValidity();

    if (!isValid) {
      signupForm.classList.add("shake");

      setTimeout(() => {
        signupForm.classList.remove("shake");
      }, 1000);
    }

    const username = usernameInput.value;
    const email = emailInput.value;
    const password = passwordInput.value;
    const confirmPassword = confirmPasswordInput.value;

    const data = {
      username,
      email,
      password,
    };

    if (password == confirmPassword) {
      const jsonData = JSON.stringify(data);
      fetch("/req/signup", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: jsonData,
      }).then((response) => {
        if (response.status == 200) {
          alert("Success");
          window.location.href = "/index";
        } else {
          alert("Error: " + response.status);
        }
      });
    } else {
      alert("Passwords do not match");
    }
  });
});
