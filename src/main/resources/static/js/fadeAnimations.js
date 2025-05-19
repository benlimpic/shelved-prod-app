document.addEventListener('DOMContentLoaded', () => {
  const messageBox = document.querySelector('.errorSuccessHandlingBox');
  const success = document.getElementById('successMessageDiv');
  const error = document.getElementById('errorMessageDiv');


  let messageDiv = null;
  let bgColor = '';
  if (success && success.textContent.trim() !== '') {
    messageDiv = success;
    bgColor = '#22c55e';
    if (error) error.classList.add('hidden');
    success.classList.remove('hidden');
  } else if (error && error.textContent.trim() !== '') {
    messageDiv = error;
    bgColor = '#ef4444';
    if (success) success.classList.add('hidden');
    error.classList.remove('hidden');
  }

  if (messageDiv && messageBox) {
    messageBox.classList.remove('hidden');
    messageBox.style.setProperty('background-color', bgColor, 'important');
    messageBox.style.position = 'fixed';
    messageBox.classList.remove('slide-out');
    void messageBox.offsetWidth;
    messageBox.classList.add('slide-in');

    setTimeout(() => {
      messageBox.classList.remove('slide-in');
      messageBox.classList.add('slide-out');
      messageBox.addEventListener('animationend', function handler() {
        messageBox.classList.add('hidden');
        messageBox.classList.remove('slide-out');
        messageBox.removeEventListener('animationend', handler);
      });
    }, 3000);
  } else if (messageBox) {
    messageBox.classList.add('hidden');
    if (success) success.classList.add('hidden');
    if (error) error.classList.add('hidden');
  }
});