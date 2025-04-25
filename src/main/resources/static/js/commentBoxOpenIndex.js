document.addEventListener('DOMContentLoaded', function () {

  
  // Check if the URL contains a hash with the format #comments-{collectionId}
  if (window.location.hash && window.location.hash.startsWith('#comments-')) {

    const collectionContainer = document.querySelector('.collectionContainer');
    const body = document.body;
    const html = document.documentElement;

    // Extract collection ID from URL
    const hash = window.location.hash;
    const collectionId = hash.split('-')[1];

    const collectionCaption = document.querySelector('.collectionCaption');
    const commentFormAction = document.querySelector('.commentFormAction');
    const footerComment = document.getElementById('commentActivity');
    const footerNavContent = document.getElementById('footerNavContent');

    
    const collectionCommentButton = document.getElementById(`commentToggle-${collectionId}`);
    const collectionCommentBox = document.getElementById(`collectionCommentBox-${collectionId}`);
    const collectionCommentBoxExit = document.getElementById(`collectionCommentBoxExit-${collectionId}`);


    //SHOW THE COMMENT BOX AND FOOTER COMMENT

    // Collection Full Screen
    collectionContainer.classList.add('fullscreen');

    // Disable Page Scrolling
    body.classList.add('overflow-hidden');
    html.classList.add('overflow-hidden');

    // Show Comment Box
    collectionCommentBox.classList.remove('hidden');



    //COMMENT BOX AND WINDOW RESIZING

    // Calculate Comment Box Height
    const footer = document.querySelector('footer');
    const footerTop = footer.getBoundingClientRect().top;
    const captionBottom = collectionCaption.getBoundingClientRect().bottom;
    const availableHeight = Math.max(0, footerTop - captionBottom);

    // Set the height of the comment box
    collectionCommentBox.style.maxHeight = `${availableHeight}px`;

    // Add Scrolling to Comment Box
    collectionCommentBox.style.overflowY = 'auto';

    // Add padding to the bottom of the comment box
    collectionCommentBox.style.paddingBottom = '20px';

    // Footer Content
    footerNavContent.classList.add('hidden');
    footerComment.classList.remove('hidden');
    collectionCommentBoxExit.classList.remove('hidden');

    

    //PASS THE COLLECTION ID TO THE FOOTER COMMENT

    const commentFormInput = document.querySelector(
      '.commentFormCollectionIdInput'
    );
    if (commentFormInput) {
      commentFormInput.setAttribute('value', collectionId);
    }

    // Set the action attribute of the form to include the collection ID
    const commentForm = document.querySelector('.commentForm');
    if (commentForm) {
      commentForm.setAttribute(
        'action',
        `/collections/${collectionId}/comments-from-index`
      );
    }


    



    // COMMENT BOX EXIT

    collectionCommentBoxExit.addEventListener('click', () => {
      // Reset Index Page
      collectionCommentBox.classList.add('hidden');
      collectionCommentBoxExit.classList.add('hidden');
      body.classList.remove('overflow-hidden');
      html.classList.remove('overflow-hidden');
      collectionContainer.classList.remove('fullscreen');
      footerNavContent.classList.remove('hidden');
      footerComment.classList.add('hidden');
  
      // Clear the footer content
      footerComment.removeAttribute('data-collection-id');
      commentFormAction.setAttribute('action', '');
      commentFormInput.setAttribute('value', '');
      });

  }
  else {
    
    console.log("Comment box with id 'comments' not found.");
  }

});