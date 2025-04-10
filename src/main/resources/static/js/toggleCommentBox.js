const commentToggle = document.getElementById('commentToggle');
const commentBox = document.getElementById('commentBox');

if (commentToggle && commentBox) {
  commentToggle.addEventListener('click', () => {
    if (commentBox.classList.contains('hidden')) {
      // Show the comment box
      commentBox.classList.remove('hidden');
      const offset = commentToggle.getBoundingClientRect().bottom;
      commentBox.style.height = `calc(100vh - ${offset}px)`;

      // Disable body scrolling
      document.body.style.overflow = 'hidden';
    } else {
      // Hide the comment box
      commentBox.classList.add('hidden');
      commentBox.style.height = '0';

      // Re-enable body scrolling
      document.body.style.overflow = 'auto';
    }
  });
}