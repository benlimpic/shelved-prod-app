// This script handles the delete account functionality on the update profile page.

document.addEventListener('DOMContentLoaded', () => {
  const deleteAccountForm = document.getElementById('deleteAccountForm');
  const confirmDeleteInput = document.getElementById('confirmDelete');
  const deleteErrorDiv = document.getElementById('deleteError');
  const deleteAccountButton = document.getElementById('deleteAccountButton');
  const cancelDeleteButton = document.getElementById('cancelDeleteButton');
  const confirmDeleteLabel = document.getElementById('confirmDeleteLabel');

  // Show the delete account form when the button is clicked
  deleteAccountButton.addEventListener('click', () => {
    deleteAccountForm.classList.toggle('hidden');
    deleteAccountButton.classList.toggle('hidden');
  });

  cancelDeleteButton.addEventListener('click', () => {
    deleteAccountForm.classList.toggle('hidden');
    deleteAccountButton.classList.toggle('hidden');
  });

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
