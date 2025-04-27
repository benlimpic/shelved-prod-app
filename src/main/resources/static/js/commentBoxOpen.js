document.addEventListener("DOMContentLoaded", function () {
  // Check if the URL contains the #comments hash
  if (window.location.hash && window.location.hash.includes("#comments")) {
      const commentBox = document.getElementById("commentBox");
      const collectionCommentBoxExit = document.querySelector(".collectionCommentBoxExit");
      const itemGrid = document.getElementById('itemGrid');
      const footerContent = document.getElementById('footerNavContent');
      const footerComment = document.getElementById('commentActivity');

      if (commentBox) {
          // Ensure the comment box is visible (if hidden by default)
          if (commentBox.classList.contains("hidden")) {
                      // Show the comment box
            commentBox.classList.remove('hidden');

            const offset = commentToggle.getBoundingClientRect().bottom;
            commentBox.style.height = `calc(100vh - ${offset}px)`;

            // Hide the item grid
            itemGrid.classList.add('hidden');
            footerContent.classList.add('hidden');
            footerComment.classList.remove('hidden');

            // Disable body scrolling
            document.body.style.overflow = 'hidden';
            collectionCommentBoxExit.classList.remove('hidden');
          }

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
          console.error("Comment box with id 'comments' not found.");
      }
  }
});