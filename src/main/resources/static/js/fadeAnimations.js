document.addEventListener('DOMContentLoaded', () => {
    const errorMessageDiv = document.getElementById('errorMessageDiv'); // Get the error message div
    const successMessageDiv = document.getElementById('successMessageDiv'); // Get the success message div
  
    // Function to show an error message with fade-in and fade-out animation
    function showError(message) {
      errorMessageDiv.textContent = message; // Set the error message
      errorMessageDiv.classList.remove('hidden', 'fade-out'); // Ensure it's visible and remove fade-out
      errorMessageDiv.classList.add('fade-in'); // Add fade-in animation
  
      // Listen for the end of the fade-in transition
      errorMessageDiv.addEventListener('transitionend', () => {
        // Start the fade-out animation after the fade-in completes
        errorMessageDiv.classList.remove('fade-in');
        errorMessageDiv.classList.add('fade-out');
  
        // Listen for the end of the fade-out transition
        errorMessageDiv.addEventListener('transitionend', () => {
          errorMessageDiv.classList.add('hidden'); // Hide the element after fade-out
        }, { once: true }); // Ensure the listener is only triggered once
      }, { once: true }); // Ensure the listener is only triggered once
    }
  
    // Function to show a success message with fade-in and fade-out animation
    function showSuccess(message) {
      successMessageDiv.textContent = message; // Set the success message
      successMessageDiv.classList.remove('hidden', 'fade-out'); // Ensure it's visible and remove fade-out
      successMessageDiv.classList.add('fade-in'); // Add fade-in animation
  
      // Listen for the end of the fade-in transition
      successMessageDiv.addEventListener('transitionend', () => {
        // Start the fade-out animation after the fade-in completes
        successMessageDiv.classList.remove('fade-in');
        successMessageDiv.classList.add('fade-out');
  
        // Listen for the end of the fade-out transition
        successMessageDiv.addEventListener('transitionend', () => {
          successMessageDiv.classList.add('hidden'); // Hide the element after fade-out
        }, { once: true }); // Ensure the listener is only triggered once
      }, { once: true }); // Ensure the listener is only triggered once
    }
  
    // Trigger error or success animations if messages are present
    if (errorMessageDiv && errorMessageDiv.textContent.trim() !== '') {
      showError(errorMessageDiv.textContent);
    }
  
    if (successMessageDiv && successMessageDiv.textContent.trim() !== '') {
      showSuccess(successMessageDiv.textContent);
    }
  });