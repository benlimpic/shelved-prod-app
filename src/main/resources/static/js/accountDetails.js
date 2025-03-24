
document.addEventListener('DOMContentLoaded', () => {
  //ACCOUNT DETAILS HEADER
  const accountDetailsHeader = document.getElementById('accountDetailsHeader');

// ----------------------------------------------------------------------------- 

  // UPDATE USERNAME
  const updateUsernameButton = document.getElementById('updateUsernameButton');
  const updateUsernameForm = document.getElementById('updateUsernameForm');
  const cancelUpdateUsernameButton = document.getElementById('cancelUpdateUsernameButton');

  // Show the update username form when the button is clicked
  updateUsernameButton.addEventListener('click', () => {
    updateUsernameForm.classList.toggle('hidden');
    accountDetailsHeader.textContent = 'Update Username';
    updateUsernameButton.classList.toggle('hidden');
    updateEmailButton.classList.toggle('hidden');
    updatePasswordButton.classList.toggle('hidden');
    deleteAccountButton.classList.toggle('hidden');
  });

  // Hide the update username form when the cancel button is clicked
  cancelUpdateUsernameButton.addEventListener('click', () => {
    updateUsernameForm.classList.toggle('hidden');
    accountDetailsHeader.textContent = 'Account Details';
    updateUsernameButton.classList.toggle('hidden');
    updateEmailButton.classList.toggle('hidden');
    updatePasswordButton.classList.toggle('hidden');
    deleteAccountButton.classList.toggle('hidden');
  });

// ----------------------------------------------------------------------------- 

  // UPDATE EMAIL
  const updateEmailButton = document.getElementById('updateEmailButton');
  const updateEmailForm = document.getElementById('updateEmailForm');
  const cancelUpdateEmailButton = document.getElementById('cancelUpdateEmailButton');

  // Show the update email form when the button is clicked
  updateEmailButton.addEventListener('click', () => {
    updateEmailForm.classList.toggle('hidden');
    accountDetailsHeader.textContent = 'Update Email';
    updateEmailButton.classList.toggle('hidden');
    updateUsernameButton.classList.toggle('hidden');
    updatePasswordButton.classList.toggle('hidden');
    deleteAccountButton.classList.toggle('hidden');
  });

  // Hide the update email form when the cancel button is clicked
  cancelUpdateEmailButton.addEventListener('click', () => {
    updateEmailForm.classList.toggle('hidden');
    accountDetailsHeader.textContent = 'Account Details';
    updateEmailButton.classList.toggle('hidden');
    updateUsernameButton.classList.toggle('hidden');
    updatePasswordButton.classList.toggle('hidden');
    deleteAccountButton.classList.toggle('hidden');
  });

// -----------------------------------------------------------------------------
  
  // UPDATE PASSWORD
  const updatePasswordButton = document.getElementById('updatePasswordButton');
  const updatePasswordForm = document.getElementById('updatePasswordForm');
  const cancelUpdatePasswordButton = document.getElementById
  ('cancelUpdatePasswordButton');
  
  // Show the update password form when the button is clicked
  updatePasswordButton.addEventListener('click', () => {
    updatePasswordForm.classList.toggle('hidden');
    accountDetailsHeader.textContent = 'Update Password';
    updatePasswordButton.classList.toggle('hidden');
    updateUsernameButton.classList.toggle('hidden');
    updateEmailButton.classList.toggle('hidden');
    deleteAccountButton.classList.toggle('hidden');
  });

  // Hide the update password form when the cancel button is clicked
  cancelUpdatePasswordButton.addEventListener('click', () => {
    updatePasswordForm.classList.toggle('hidden');
    accountDetailsHeader.textContent = 'Account Details';
    updatePasswordButton.classList.toggle('hidden');
    updateUsernameButton.classList.toggle('hidden');
    updateEmailButton.classList.toggle('hidden');
    deleteAccountButton.classList.toggle('hidden');
  });

  // -----------------------------------------------------------------------------
  
  // DELETE ACCOUNT
  const deleteAccountForm = document.getElementById('deleteAccountForm');
  const confirmDeleteInput = document.getElementById('confirmDelete');
  const deleteErrorDiv = document.getElementById('deleteError');
  const deleteAccountButton = document.getElementById('deleteAccountButton');
  const cancelDeleteButton = document.getElementById('cancelDeleteButton');
  const confirmDeleteLabel = document.getElementById('confirmDeleteLabel');

  // Show the delete account form when the button is clicked
  deleteAccountButton.addEventListener('click', () => {
    deleteAccountForm.classList.toggle('hidden');
    accountDetailsHeader.textContent = 'Delete Account';
    deleteAccountButton.classList.toggle('hidden');
    updateUsernameButton.classList.toggle('hidden');
    updateEmailButton.classList.toggle('hidden');
    updatePasswordButton.classList.toggle('hidden');
  });

  // Hide the delete account form when the cancel button is clicked
  cancelDeleteButton.addEventListener('click', () => {
    deleteAccountForm.classList.toggle('hidden');
    accountDetailsHeader.textContent = 'Account Details';
    deleteAccountButton.classList.toggle('hidden');
    updateUsernameButton.classList.toggle('hidden');
    updateEmailButton.classList.toggle('hidden');
    updatePasswordButton.classList.toggle('hidden');
  });

  // Validate the delete account form when submitted
  deleteAccountForm.addEventListener('submit', (event) => {       
    if (confirmDeleteInput.value !== 'DELETE') {
      event.preventDefault();
      confirmDeleteLabel.textContent = 'Type "DELETE" To Confirm';
      deleteErrorDiv.classList.remove('hidden');
      confirmDeleteInput.classList.add('border-red-500', 'ring-red-500');
    } else {
      confirmDeleteInput.classList.remove('border-red-500', 'ring-red-500');
    }
  });

});
