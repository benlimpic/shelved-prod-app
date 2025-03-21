document.addEventListener('DOMContentLoaded', () => {

  const updateProfilePhotoForm = document.getElementById('updateProfilePhotoForm');
  const updateProfilePhotoButton = document.getElementById('updateProfilePhotoButton');
  const cancelProfilePhotoUpdateButton = document.getElementById('cancelProfilePhotoUpdateButton');

  // Show the update profile photo form when the button is clicked
  updateProfilePhotoButton.addEventListener('click', () => {
    updateProfilePhotoForm.classList.toggle('hidden');
    updateProfilePhotoButton.classList.toggle('hidden');
  }
  );
  // Hide the form when the cancel button is clicked
  cancelProfilePhotoUpdateButton.addEventListener('click', () => {
    updateProfilePhotoForm.classList.toggle('hidden');
    updateProfilePhotoButton.classList.toggle('hidden');
  });

});