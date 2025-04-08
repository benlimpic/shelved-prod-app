document.addEventListener('DOMContentLoaded', () => {
  const saveChangesButton = document.getElementById('saveChangesButton'); // Button in the nav
  const hiddenSubmitButton = document.getElementById('hiddenSubmitButton'); // Hidden submit button in the form

  // Add click event listener to the nav button
  saveChangesButton.addEventListener('click', () => {
    hiddenSubmitButton.click(); // Programmatically click the hidden submit button
  });
});

