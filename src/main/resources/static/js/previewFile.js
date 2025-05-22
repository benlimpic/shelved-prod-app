const fileInput = document.getElementById('hiddenFileInput');
const previewImage = document.getElementById('previewImage');
const triggerUpload = document.getElementById('triggerUpload');

const createCollectionForm = document.getElementById('createCollectionForm');
const createItemForm = document.getElementById('createItemForm');
const updateProfileForm = document.getElementById('updateProfileForm');
const updateCollectionForm = document.getElementById('updateCollectionForm');
const updateItemForm = document.getElementById('updateItemForm');
let processedBlob = null;

fileInput.addEventListener('change', function () {
  const file = this.files[0];

  if (file) {
    const reader = new FileReader();

    reader.onload = function (e) {
      const img = new Image();
      img.onload = function () {
        EXIF.getData(file, function() {
          const orientation = EXIF.getTag(this, "Orientation") || 1;
          const size = Math.min(img.width, img.height);

          // Default canvas size
          let canvasWidth = 500;
          let canvasHeight = 500;

          // Swap width/height for 90-degree rotations
          let destWidth = canvasWidth;
          let destHeight = canvasHeight;
          if (orientation === 6 || orientation === 8) {
            [canvasWidth, canvasHeight] = [canvasHeight, canvasWidth];
            [destWidth, destHeight] = [destHeight, destWidth];
          }

          const canvas = document.createElement('canvas');
          canvas.width = canvasWidth;
          canvas.height = canvasHeight;
          const ctx = canvas.getContext('2d');

          applyOrientationTransform(ctx, orientation, canvasWidth, canvasHeight);

          ctx.drawImage(
            img,
            (img.width - size) / 2,
            (img.height - size) / 2,
            size,
            size,
            0,
            0,
            destWidth,
            destHeight
          );

          canvas.toBlob(function (blob) {
            if (blob) {
              const processedFile = new File([blob], 'collection.jpg', { type: 'image/jpeg' });
              const dataTransfer = new DataTransfer();
              dataTransfer.items.add(processedFile);

              const closestForm = fileInput.closest('form');
              if (closestForm) {
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
    default:
      break;
  }
}


document
  .getElementById('triggerUpload')
  .addEventListener('click', function (event) {
    event.preventDefault(); // Prevent the anchor from navigating
    document.getElementById('hiddenFileInput').click();
  });
