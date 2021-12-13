const presetEnv = require("postcss-preset-env");
const autoprefixer = require("autoprefixer");
const fontMagician = require("postcss-font-magician");
const stylelint = require("stylelint");
const reporter = require("postcss-reporter");

module.exports = {
	plugins: [
    // stylelint(),
    presetEnv({
      browsers: "last 2 versions",
      stage: 3,

      features: {
        "nesting-rules": true
      }
    }),

    autoprefixer,
    fontMagician(),
    // reporter({ clearReportedMessages: true })
  ]
};
