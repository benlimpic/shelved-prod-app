const trigger = document.getElementById("triggerUpload");
const triggerForm = document.querySelector("form#commentButton");

if (trigger) {
  trigger.addEventListener("click", () => {

    if (triggerForm) {
      triggerForm.submit();
    } else {
      console.error("Form element not found.");
    }

  });
} else {
  console.error("Trigger element not found.");
}