module.exports = {
  devServer: {
    watchOptions: {
      ignored: /node_modules|dist|logs|target/
    }
  },
  content: [
    "./src/main/resources/templates/**/*.html", // HTML templates
    "./src/main/resources/static/**/*.js",      // Static JS files
    "./src/main/resources/static/**/*.css",     // Static CSS files
    "!./src/main/resources/static/css/output.css", // Exclude the output.css file explicitly
    "!./target/**",                              // Exclude Spring Boot's build directory
  ],
  theme: {
    extend: {},
  },
  plugins: [
    require('@tailwindcss/forms')({
      strategy: 'base',
    }),
    require('tailwind-scrollbar-hide'),
  ],
};