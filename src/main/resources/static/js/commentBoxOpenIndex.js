document.addEventListener('DOMContentLoaded', function () {
  if (window.location.hash) {
    const hash = window.location.hash.replace('#comments-', ''); // Extract the collection ID
    console.log(`Hash: ${hash}`);

    const body = document.body;
    const html = document.documentElement;

    const collectionContainer = document.getElementById(hash);
    if (!collectionContainer) {
      console.error(`No collectionContainer found with id: ${hash}`);
      return; // Exit if the container is not found
    }

    console.log(`Found collectionContainer with id: ${hash}`);
    collectionContainer.scrollIntoView({
      behavior: 'smooth',
      block: 'start',
    });

    // Make the collection fullscreen
    collectionContainer.classList.add('fullscreen');
    // Disable Page Scrolling
    body.classList.add('overflow-hidden');
    html.classList.add('overflow-hidden');

    // Unhide the comment navigation
    const footerComment = document.getElementById('commentActivity');
    const footerNavContent = document.getElementById('footerNavContent');
    const collectionCommentBoxExit = collectionContainer.querySelector(
      '.collectionCommentBoxExit'
    );
    const collectionCommentBox = collectionContainer.querySelector(
      '.collectionCommentBox'
    );
    

    if (footerComment && footerNavContent) {
      footerNavContent.classList.add('hidden');
      footerComment.classList.remove('hidden');
      collectionCommentBox.classList.remove('hidden');
      collectionCommentBoxExit.classList.remove('hidden');
    } else {
      console.error('Footer elements not found');
    }


    const commentForm = document.querySelector('.commentForm'); // Ensure this is the correct selector
    if (!commentForm) {
      console.error('CommentForm not found');
      return;
    }



// THIS SHIT SUCKS AND IS BROKEN AS FUCK

// ----------------------------------------------------------------------------

    const commentFormAction = commentForm.querySelector('.commentFormAction');
    const commentFormInput = commentForm.querySelector('.commentFormCollectionIdInput');
    if (commentFormAction && commentFormInput) {
      commentFormAction.setAttribute('action', `/collections/${hash}/comments-from-index`);
      commentFormInput.setAttribute('value', hash);
    } else {
      console.error('Comment form action or input not found');
    }

// ----------------------------------------------------------------------------





    // Exit Comment Box
    collectionCommentBoxExit.addEventListener('click', () => {
      // Reset Index Page
      if (collectionCommentBox) {
        collectionCommentBox.classList.add('hidden');
      }
      if (collectionCommentBoxExit) {
        collectionCommentBoxExit.classList.add('hidden');
      }
      body.classList.remove('overflow-hidden');
      html.classList.remove('overflow-hidden');
      if (collectionContainer) {
        collectionContainer.classList.remove('fullscreen');
      }
      if (footerNavContent) {
        footerNavContent.classList.remove('hidden');
      }
      if (footerComment) {
        footerComment.classList.add('hidden');
        // Clear the footer content
        footerComment.removeAttribute('data-collection-id');
      }
      if (commentFormAction) {
        commentFormAction.setAttribute('action', '');
      }
      if (commentFormInput) {
        commentFormInput.setAttribute('value', '');
      }
    });
  }
});

document.addEventListener('DOMContentLoaded', function () {
  if (window.location.hash) {
    const hash = window.location.hash.replace('#comments-', ''); // Extract the collection ID
    console.log(`Extracted hash: ${hash}`);

    const collectionContainer = document.getElementById(hash);
    if (!collectionContainer) {
      console.error(`No collectionContainer found with id: ${hash}`);
      return; // Exit if the container is not found
    }

    console.log(`Found collectionContainer with id: ${hash}`);

    const commentForm = document.querySelector('.commentForm'); // Ensure this is the correct selector
    if (!commentForm) {
      console.error('CommentForm not found');
      return;
    }

    const commentFormAction = commentForm.querySelector('.commentFormAction');
    const commentFormInput = commentForm.querySelector('.commentFormCollectionIdInput');

    if (commentFormAction) {
      commentFormAction.setAttribute('action', `/collections/${hash}/comments-from-index`);
      console.log(`Set action to: ${commentFormAction.getAttribute('action')}`);
    } else {
      console.error('CommentFormAction not found');
    }

    if (commentFormInput) {
      commentFormInput.setAttribute('value', hash);
      console.log(`Set value to: ${commentFormInput.getAttribute('value')}`);
    } else {
      console.error('CommentFormCollectionIdInput not found');
    }
  }
});