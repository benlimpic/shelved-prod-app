document.addEventListener('DOMContentLoaded', () => {
  const commentToggle = document.getElementById('commentToggle');
  const collectionCommentBoxExit = document.querySelector('.collectionCommentBoxExit');
  const commentBox = document.getElementById('commentBox');
  const itemGrid = document.getElementById('itemGrid');


  const footerContent = document.getElementById('footerNavContent');
  const footerComment = document.getElementById('commentActivity');


  if (commentToggle && commentBox && itemGrid) {
    commentToggle.addEventListener('click', () => {

      if (commentBox.classList.contains('hidden')) {
        // Show the comment box
        commentBox.classList.remove('hidden');
        const offset = commentToggle.getBoundingClientRect().bottom;
        commentBox.style.height = `calc(100vh - ${offset}px)`;

        // Hide the item grid
        itemGrid.classList.add('hidden');
        collectionCommentBoxExit.classList.remove('hidden');
        footerContent.classList.add('hidden');
        footerComment.classList.remove('hidden');

        // Disable body scrolling
        document.body.style.overflow = 'hidden';

        collectionCommentBoxExit.addEventListener('click', () => {
          // Hide the comment box
          commentBox.classList.add('hidden');
          commentBox.style.height = '0';

          // Show the item grid
          itemGrid.classList.remove('hidden');
          footerContent.classList.remove('hidden');
          footerComment.classList.add('hidden');
          collectionCommentBoxExit.classList.add('hidden');

          // Re-enable body scrolling
          document.body.style.overflow = 'auto';
        });


      } else {
        // Hide the comment box
        commentBox.classList.add('hidden');
        commentBox.style.height = '0';

        // Show the item grid
        itemGrid.classList.remove('hidden');
        footerContent.classList.remove('hidden');
        footerComment.classList.add('hidden');

        // Re-enable body scrolling
        document.body.style.overflow = 'auto';
      }
    });
  }
});