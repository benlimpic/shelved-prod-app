document.addEventListener('DOMContentLoaded', () => {
  // Select all toggle buttons
  const commentToggles = document.querySelectorAll('.commentToggle');

  commentToggles.forEach((toggle) => {
    toggle.addEventListener('click', () => {
      // Get the parent collection container
      const collectionContainer = toggle.closest('.collectionContainer');
      const commentBox = collectionContainer.querySelector('.commentBox');
              const footerContent = document.getElementById('footerNavContent');
              const footerComment = document.getElementById('commentActivity');

      if (!commentBox.classList.contains('visible')) {

        // Show the comment box
        commentBox.classList.add('visible');
        footerContent.classList.add('hidden');
        footerComment.classList.remove('hidden');

        // Disable scrolling for the rest of the page
        document.body.style.overflow = 'hidden';
        document.documentElement.style.overflow = 'hidden'; // Ensure html element is also affected
        document.body.classList.add('no-scroll'); // Add class to disable scrolling

        // Expand the collectionContainer to full viewport height
        collectionContainer.style.height = '100vh';
        collectionContainer.style.overflow = 'hidden'; // Prevent scrolling within the container

        // Center the collectionContainer in the viewport
        collectionContainer.scrollIntoView({ behavior: 'smooth', block: 'center' });



      } else {



        // Hide the comment box
        commentBox.classList.remove('visible');
        footerContent.classList.remove('hidden');
        footerComment.classList.add('hidden');

        // Re-enable scrolling for the rest of the page
        document.body.style.overflow = '';
        document.documentElement.style.overflow = ''; // Re-enable scrolling for html element
        document.body.classList.remove('no-scroll'); // Remove class to re-enable scrolling

        // Reset the collectionContainer height
        collectionContainer.style.height = '';
        collectionContainer.style.overflow = '';
      }
    });
  });
});

