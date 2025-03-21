document.addEventListener('DOMContentLoaded', () => {
  const errorMessageDiv = document.getElementById('errorMessageDiv'); // Get the error message div
  const successMessageDiv = document.getElementById('successMessageDiv'); // Get the success message div

  // Function to show an error message with fade-in and fade-out animation
  function showError(message) {
      errorMessageDiv.textContent = message; // Set the error message
      errorMessageDiv.classList.remove('hidden', 'fade-out'); // Ensure it's visible and remove fade-out
      errorMessageDiv.classList.add('fade-in'); // Add fade-in animation

      // After 3 seconds, fade out the error message
      setTimeout(() => {
          errorMessageDiv.classList.remove('fade-in'); // Remove fade-in
          errorMessageDiv.classList.add('fade-out'); // Add fade-out animation
          setTimeout(() => {
              errorMessageDiv.classList.add('hidden');
          }, 500); // Match the duration of the fade-out animation
      }, 3000); // Wait 3 seconds before fading out
  }

  // Function to show a success message with fade-in and fade-out animation
  function showSuccess(message) {
      successMessageDiv.textContent = message; // Set the success message
      successMessageDiv.classList.remove('hidden', 'fade-out'); // Ensure it's visible and remove fade-out
      successMessageDiv.classList.add('fade-in'); // Add fade-in animation

      // After 3 seconds, fade out the success message
      setTimeout(() => {
          successMessageDiv.classList.remove('fade-in'); // Remove fade-in
          successMessageDiv.classList.add('fade-out'); // Add fade-out animation
          setTimeout(() => {
              successMessageDiv.classList.add('hidden');
          }, 500); // Match the duration of the fade-out animation
      }, 3000); // Wait 3 seconds before fading out
  }

  // Trigger error or success animations if messages are present
  if (errorMessageDiv && errorMessageDiv.textContent.trim() !== '') {
      showError(errorMessageDiv.textContent);
  }

  if (successMessageDiv && successMessageDiv.textContent.trim() !== '') {
      showSuccess(successMessageDiv.textContent);
  }
});