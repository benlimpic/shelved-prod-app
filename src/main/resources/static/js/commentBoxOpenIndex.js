document.addEventListener('DOMContentLoaded', function () {
  if (window.location.hash) {

    // EXTRACT HASH FROM URL
    const hash = window.location.hash.replace('#comments-', ''); // Extract the collection ID
    console.log(`Hash: ${hash}`);

    // SELECT PAGE ELEMENTS
    const body = document.body;
    const html = document.documentElement;
    const collectionContainer = document.getElementById(hash);

    // SCROLL TO COLLECTION CONTAINER
    collectionContainer.scrollIntoView({
      behavior: 'smooth',
      block: 'start',
    });

    // PAGE SETTINGS
    collectionContainer.classList.add('fullscreen');
    body.classList.add('overflow-hidden');
    html.classList.add('overflow-hidden');

    // SELECT COMMENT COMPONENTS
    const footerComment = document.getElementById('commentActivity');
    const footerNavContent = document.getElementById('footerNavContent');
    const collectionCommentBoxExit = collectionContainer.querySelector('.collectionCommentBoxExit');
    const collectionCommentBox = collectionContainer.querySelector('.collectionCommentBox');
    
    // UNHIDE COMMENT COMPONENTS
    footerNavContent.classList.add('hidden');
    footerComment.classList.remove('hidden');
    collectionCommentBox.classList.remove('hidden');
    collectionCommentBoxExit.classList.remove('hidden');

    // SELECT FORM COMPONENTS
    const commentForm = document.querySelector('.commentForm');
    const commentFormAction = commentForm.querySelector('.commentFormAction');
    const commentFormInput = commentForm.querySelector('.commentFormCollectionIdInput');

    // SET FORM VALUES
    commentForm.setAttribute('action', `/collections/${hash}/comments-from-index`);
    commentFormInput.setAttribute('value', hash);

    
    // EXIT BUTTON
    collectionCommentBoxExit.addEventListener('click', () => {

      // RESET PAGE SETTINGS
      body.classList.remove('overflow-hidden');
      html.classList.remove('overflow-hidden');
      collectionContainer.classList.remove('fullscreen');

      //RESET COMMENT COMPONENTS
      collectionCommentBox.classList.add('hidden');
      collectionCommentBoxExit.classList.add('hidden');
      footerNavContent.classList.remove('hidden');
      footerComment.classList.add('hidden');

      // CLEAR FOOTER COMMENT DATA
      footerComment.removeAttribute('data-collection-id');
      commentFormAction.setAttribute('action', '');
      commentFormInput.setAttribute('value', '');
      
    });
  }
});

