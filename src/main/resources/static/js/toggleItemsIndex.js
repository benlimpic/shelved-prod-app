document.addEventListener('DOMContentLoaded', () => {
  const showButtons = document.querySelectorAll('.toggleButtonShowItems');
  const hideButtons = document.querySelectorAll('.toggleButtonHideItems');
  const overlays = document.querySelectorAll('.collectionOverlay');

  showButtons.forEach((button, index) => {
    button.addEventListener('click', () => {
      overlays[index].classList.remove('hidden');
      button.classList.add('hidden');
      hideButtons[index].classList.remove('hidden');
    });
  });

  hideButtons.forEach((button, index) => {
    button.addEventListener('click', () => {
      overlays[index].classList.add('hidden');
      button.classList.add('hidden');
      showButtons[index].classList.remove('hidden');
    });
  });
});