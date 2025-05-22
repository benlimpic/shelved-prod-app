const fileInput = document.getElementById('hiddenFileInput');
const previewImage = document.getElementById('previewImage');
const triggerUpload = document.getElementById('triggerUpload');

let processedBlob = null;

fileInput.addEventListener('change', function () {
  const file = this.files[0];

  if (file) {
    const reader = new FileReader();

    reader.onload = function (e) {
      const img = new Image();
      img.onload = function () {
        // Read EXIF orientation
        EXIF.getData(file, function() {
          const orientation = EXIF.getTag(this, "Orientation") || 1;
          const canvas = document.createElement('canvas');
          const ctx = canvas.getContext('2d');
          const size = Math.min(img.width, img.height);
          canvas.width = 500;
          canvas.height = 500;

          // Apply orientation transform
          applyOrientationTransform(ctx, orientation, canvas.width, canvas.height);

          ctx.drawImage(
            img,
            (img.width - size) / 2,
            (img.height - size) / 2,
            size,
            size,
            0,
            0,
            canvas.width,
            canvas.height
          );

          canvas.toBlob(function (blob) {
            if (blob) {
              const processedFile = new File([blob], 'collection.jpg', { type: 'image/jpeg' });
              const dataTransfer = new DataTransfer();
              dataTransfer.items.add(processedFile);

              // Find the closest form to the file input
              const closestForm = fileInput.closest('form');
              if (closestForm) {
                // Find the file input within the closest form and set its files
                const formFileInput = closestForm.querySelector('input[type="file"]');
                if (formFileInput) {
                  formFileInput.files = dataTransfer.files;
                }
              }

              previewImage.src = canvas.toDataURL('image/jpeg');
            } else {
              console.error('Failed to create blob from canvas.');
            }
          }, 'image/jpeg', 0.95);
        });
      };
      img.src = e.target.result;
    };
    reader.readAsDataURL(file);
  }
});

triggerUpload.addEventListener('click', function (event) {
  event.preventDefault(); // Prevent the anchor from navigating
  fileInput.click();
});

// Helper function for orientation
function applyOrientationTransform(ctx, orientation, width, height) {
  switch (orientation) {
    case 3:
      ctx.translate(width, height);
      ctx.rotate(Math.PI);
      break;
    case 6:
      ctx.translate(width, 0);
      ctx.rotate(Math.PI / 2);
      break;
    case 8:
      ctx.translate(0, height);
      ctx.rotate(-Math.PI / 2);
      break;
    // Add more cases if needed
    default:
      // No transform needed
      break;
  }
}