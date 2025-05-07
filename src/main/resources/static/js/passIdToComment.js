

  const triggerCommentId = (button) => {
    const commentId = button.getAttribute('data-comment-id'); // Get the comment ID from the clicked button
    const commentReplyActivity = document.getElementById('commentReplyActivity');
    const commentReplyForm = document.getElementById('commentReplyForm');
    const commentActivity = document.getElementById('commentActivity');
    const commentIdInput = document.getElementById('replyCommentIdInput');
  
    if (commentReplyActivity) {
      commentReplyForm.setAttribute('action', `/comments/${commentId}/replies`); // Set the form action dynamically
      console.log(`Form action set to: /comments/${commentId}/replies`);
    } else {
      console.error("Form with ID 'commentReplyActivity' not found.");
    }
  
    if (commentIdInput) {
      commentIdInput.setAttribute('value', commentId); // Set the hidden input value
      console.log(`Hidden input value set to: ${commentId}`);
    } else {
      console.error("Hidden input with ID 'replyCommentIdInput' not found.");
    }

  

  if (commentActivity && commentReplyActivity) {
    if (!commentActivity.classList.contains('hidden')) {
      commentActivity.classList.add('hidden');
      commentReplyActivity.classList.remove('hidden');
    } else {
      commentActivity.classList.remove('hidden');
      commentReplyActivity.classList.add('hidden');
    }
  } else {
    console.error("Comment activity or reply activity element not found.");
  }
};


const triggerFooterFlip = () => {
  console.log("Reply button clicked");
  const commentActivity = document.getElementById('commentActivity');
  const commentReplyActivity = document.getElementById('commentReplyActivity');

  if (commentActivity && commentReplyActivity) {
    if (!commentActivity.classList.contains('hidden')) {
      commentActivity.classList.add('hidden');
      commentReplyActivity.classList.remove('hidden');
    } else {
      commentActivity.classList.remove('hidden');
      commentReplyActivity.classList.add('hidden');
    }
  } else {
    console.error("Comment activity or reply activity element not found.");
  }
};