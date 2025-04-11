const replyButton = document.getElementById('replyButton');
const replyDiv = document.getElementById('replyDiv');

replyButton.addEventListener('click', () => {
  if (replyDiv.classList.contains('hidden')) {
    // Show the reply div
    replyDiv.classList.remove('hidden');
    const offset = replyButton.getBoundingClientRect().bottom;
    replyDiv.style.height = `calc(100vh - ${offset}px)`;
  }
});