document.addEventListener('DOMContentLoaded', () => {
  const collectionContainer = document.querySelectorAll('.collectionContainer');
  const body = document.body;
  const html = document.documentElement;
  
  // Iterate over each collectionContainer
  collectionContainer.forEach((collectionContainer) => {
    
    const collectionId = collectionContainer.id;
    const collectionContainerId = document.getElementById(collectionId);
    const collectionCommentButtonId = document.getElementById('collectionCommentButton-' + collectionId);
    const collectionCommentBoxId = document.getElementById('collectionCommentBox-' + collectionId);
    const collectionCommentBoxExitId = document.getElementById('collectionCommentBoxExit-' + collectionId);
    const collectionCaptionId = document.getElementById('collectionCaption-' + collectionId);
    const footerNavContent = document.getElementById('footerNavContent');
    const footerComment = document.getElementById('commentActivity');
    const commentFormAction = collectionContainerId.querySelector('.commentFormAction');
    const commentFormInput = collectionContainerId.querySelector('.commentFormCollectionIdInput');
    

    collectionCommentButtonId.addEventListener('click', () => {
      
      if (collectionCommentBoxId.classList.contains('hidden')) {
        
        // Disable Page Scrolling
        body.classList.add('overflow-hidden');
        html.classList.add('overflow-hidden');

        // Collection Full Screen
        collectionContainerId.classList.add('fullscreen');

        // Show Comment Box
        collectionCommentBoxId.classList.remove('hidden');

        // Show Comment Box Exit
        collectionCommentBoxExitId.classList.remove('hidden');

        // Pass the collectionContainer.id to the footer
        if (collectionContainer.id && footerComment) {
          footerComment.setAttribute('data-collection-id', collectionContainer.id);
          if (commentFormAction) {
            commentFormAction.setAttribute('action', `/collections/${collectionContainer.id}/comments-from-index`);
            console.log(`Collection ID: ${collectionContainer.id}`);
          }
          if (commentFormInput) {
            commentFormInput.setAttribute('value', collectionContainer.id);
            console.log(`Collection ID: ${collectionContainer.id}`);
          }
        }

        // Calculate Comment Box Height
        const footer = document.querySelector('footer');
        const footerTop = footer.getBoundingClientRect().top;
        const captionBottom = collectionCaptionId.getBoundingClientRect().bottom;
        const availableHeight = Math.max(0, footerTop - captionBottom);

        // Set the height of the comment box
        collectionCommentBoxId.style.maxHeight = `${availableHeight}px`;

        // Add Scrolling to Comment Box
        collectionCommentBoxId.style.overflowY = 'auto';

        // Add padding to the bottom of the comment box
        collectionCommentBoxId.style.paddingBottom = '20px';

        // Footer Content
        footerNavContent.classList.add('hidden');
        footerComment.classList.remove('hidden');


        
        // Exit Comment Box
        collectionCommentBoxExitId.addEventListener('click', () => {
        // Reset Index Page
        collectionCommentBoxId.classList.add('hidden');
        collectionCommentBoxExitId.classList.add('hidden');
        body.classList.remove('overflow-hidden');
        html.classList.remove('overflow-hidden');
        collectionContainerId.classList.remove('fullscreen');
        footerNavContent.classList.remove('hidden');
        footerComment.classList.add('hidden');

        // Clear the footer content
        footerComment.removeAttribute('data-collection-id');
        if (commentFormAction) {
          commentFormAction.setAttribute('action', '');
        }
        if (commentFormInput) {
          commentFormInput.setAttribute('value', '');
        }
        });

      } else {
        // Reset Index Page
        collectionCommentBoxId.classList.add('hidden');
        collectionCommentBoxExitId.classList.add('hidden');
        body.classList.remove('overflow-hidden');
        html.classList.remove('overflow-hidden');
        collectionContainerId.classList.remove('fullscreen');
        footerNavContent.classList.remove('hidden');
        footerComment.classList.add('hidden');

        // Clear the footer content
        footerComment.removeAttribute('data-collection-id');
        if (commentFormAction) {
          commentFormAction.setAttribute('action', '');
        }
        if (commentFormInput) {
          commentFormInput.setAttribute('value', '');
        }
      }
    });
  });
});