module.exports = {
  content: [
    "./src/main/resources/templates/**/*.html",
    "./src/main/resources/static/**/*.css",
    "./src/main/resources/static/**/*.js",
  ],
  theme: {
    extend: {},
  },
  plugins: [
    //# DaisyUI adds class-based components and plugins to Tailwind CSS.
    //! Docs: https://daisyui.com/components/
    // require("daisyui"),

    //# Typography is a plugin that provides a set of prose classes to help you design rich text content.
    //! Docs: https://github.com/tailwindlabs/tailwindcss-typography
    // require("@tailwindcss/typography"),

    //# Animate a plugin that adds the animate.css library to your Tailwind CSS project.
    //! Docs: https://github.com/jamiebuilds/tailwindcss-animate
    // require("tailwindcss-animate"),

    //# Forms is a plugin that provides a set of form styles to help you design rich form content.
    //! Docs:https://github.com/tailwindlabs/tailwindcss-forms
    require('@tailwindcss/forms')({
      strategy: 'base', 
    }),
    //# Scrollbar is a plugin that adds a scrollbar-hide utility to your Tailwind CSS project.
    //! Docs: https://github.com/reslear/tailwind-scrollbar-hide
    require('tailwind-scrollbar-hide')
  ],
};
