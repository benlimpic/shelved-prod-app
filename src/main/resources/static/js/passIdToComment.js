

const triggerCommentId = (button) => {
  if (!button) {
    console.error("Button element is null or undefined.");
    return;
  }

  const commentId = button.getAttribute('data-comment-id');
  if (!commentId) {
    console.error("Button does not have a 'data-comment-id' attribute.");
    return;
  }

  const commentReplyActivity = document.getElementById('commentReplyActivity');
  const commentActivity = document.getElementById('commentActivity');
  const commentIdInput = document.getElementById('replyCommentIdInput');

  if (commentIdInput) {
    commentIdInput.setAttribute('value', commentId);
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
