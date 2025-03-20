document.addEventListener('DOMContentLoaded', function() {

  updateProfilePictureButton = document.getElementById('updateProfilePictureButton');
  updateProfilePictureForm = document.getElementById('updateProfilePictureForm');
  updateProfilePictureCancelButton = document.getElementById('updateProfilePictureCancelButton');

  updateUsernameButton = document.getElementById('updateUsernameButton');
  updateUsernameForm = document.getElementById('updateUsernameForm');
  updateUsernameCancelButton = document.getElementById('updateUsernameCancelButton');

  updatePasswordButton = document.getElementById('updatePasswordButton');
  updatePasswordForm = document.getElementById('updatePasswordForm');
  updatePasswordCancelButton = document.getElementById('updatePasswordCancelButton');

  updateEmailButton = document.getElementById('updateEmailButton');
  updateEmailForm = document.getElementById('updateEmailForm');
  updateEmailCancelButton = document.getElementById('updateEmailCancelButton');

  updateNameButton = document.getElementById('updateNameButton');
  updateNameForm = document.getElementById('updateNameForm');
  updateNameCancelButton = document.getElementById('updateNameCancelButton');

  updateBiographyButton = document.getElementById('updateBioButton');
  updateBiographyForm = document.getElementById('updateBioForm');
  updateBiographyCancelButton = document.getElementById('updateBioCancelButton');

  deleteAccountButton = document.getElementById('deleteAccountButton');
  deleteAccountForm = document.getElementById('deleteAccountForm');
  deleteAccountCancelButton = document.getElementById('deleteAccountCancelButton');

  // UPDATE PROFILE PICTURE
  updateProfilePictureButton.addEventListener('click', function() {
    updateProfilePictureButton.classList.toggle('hidden');
    updateProfilePictureForm.classList.toggle('hidden');
  });

  updateProfilePictureCancelButton.addEventListener('click', function() {
    updateProfilePictureButton.classList.toggle('hidden');
    updateProfilePictureForm.classList.toggle('hidden');
  });

  document.addEventListener('click', (event) => {
    if (!updateProfilePictureForm.classList.contains('hidden')) {
      if (!updateProfilePictureForm.contains(event.target) && !updateProfilePictureButton.contains(event.target)) {
        updateProfilePictureForm.classList.toggle('hidden');
        updateProfilePictureButton.classList.toggle('hidden');
      }
    }
  });

  // UPDATE USERNAME
  updateUsernameButton.addEventListener('click', function() {
    updateUsernameButton.classList.toggle('hidden');
    updateUsernameForm.classList.toggle('hidden');
  });

  updateUsernameCancelButton.addEventListener('click', function() {
    updateUsernameButton.classList.toggle('hidden');
    updateUsernameForm.classList.toggle('hidden');
  });

  document.addEventListener('click', (event) => {
    if (!updateUsernameForm.classList.contains('hidden')) {
      if (!updateUsernameForm.contains(event.target) && !updateUsernameButton.contains(event.target)) {
        updateUsernameForm.classList.toggle('hidden');
        updateUsernameButton.classList.toggle('hidden');
      }
    }
  });

  // UPDATE PASSWORD
  updatePasswordButton.addEventListener('click', function() {
    updatePasswordButton.classList.toggle('hidden');
    updatePasswordForm.classList.toggle('hidden');
  });

  updatePasswordCancelButton.addEventListener('click', function() {
    updatePasswordButton.classList.toggle('hidden');
    updatePasswordForm.classList.toggle('hidden');
  });

  document.addEventListener('click', (event) => {
    if (!updatePasswordForm.classList.contains('hidden')) {
      if (!updatePasswordForm.contains(event.target) && !updatePasswordButton.contains(event.target)) {
        updatePasswordForm.classList.toggle('hidden');
        updatePasswordButton.classList.toggle('hidden');
      }
    }
  });

  // UPDATE EMAIL
  updateEmailButton.addEventListener('click', function() {
    updateEmailButton.classList.toggle('hidden');
    updateEmailForm.classList.toggle('hidden');
  });

  updateEmailCancelButton.addEventListener('click', function() {
    updateEmailButton.classList.toggle('hidden');
    updateEmailForm.classList.toggle('hidden');
  });

  document.addEventListener('click', (event) => {
    if (!updateEmailForm.classList.contains('hidden')) {
      if (!updateEmailForm.contains(event.target) && !updateEmailButton.contains(event.target)) {
        updateEmailForm.classList.toggle('hidden');
        updateEmailButton.classList.toggle('hidden');
      }
    }
  });

  // UPDATE NAME
  updateNameButton.addEventListener('click', function() {
    updateNameButton.classList.toggle('hidden');
    updateNameForm.classList.toggle('hidden');
  });

  updateNameCancelButton.addEventListener('click', function() {
    updateNameButton.classList.toggle('hidden');
    updateNameForm.classList.toggle('hidden');
  });

  document.addEventListener('click', (event) => {
    if (!updateNameForm.classList.contains('hidden')) {
      if (!updateNameForm.contains(event.target) && !updateNameButton.contains(event.target)) {
        updateNameForm.classList.toggle('hidden');
        updateNameButton.classList.toggle('hidden');
      }
    }
  });

  // UPDATE BIOGRAPHY
  updateBiographyButton.addEventListener('click', function() {
    updateBiographyButton.classList.toggle('hidden');
    updateBiographyForm.classList.toggle('hidden');
  });

  updateBiographyCancelButton.addEventListener('click', function() {
    updateBiographyButton.classList.toggle('hidden');
    updateBiographyForm.classList.toggle('hidden');
  });

  document.addEventListener('click', (event) => {
    if (!updateBiographyForm.classList.contains('hidden')) {
      if (!updateBiographyForm.contains(event.target) && !updateBiographyButton.contains(event.target)) {
        updateBiographyForm.classList.toggle('hidden');
        updateBiographyButton.classList.toggle('hidden');
      }
    }
  });

  // DELETE ACCOUNT
  deleteAccountButton.addEventListener('click', function() {
    deleteAccountButton.classList.toggle('hidden');
    deleteAccountForm.classList.toggle('hidden');
  });

  deleteAccountCancelButton.addEventListener('click', function() {
    deleteAccountButton.classList.toggle('hidden');
    deleteAccountForm.classList.toggle('hidden');
  });

  document.addEventListener('click', (event) => {
    if (!deleteAccountForm.classList.contains('hidden')) {
      if (!deleteAccountForm.contains(event.target) && !deleteAccountButton.contains(event.target)) {
        deleteAccountForm.classList.toggle('hidden');
        deleteAccountButton.classList.toggle('hidden');
      }
    }
  });

});