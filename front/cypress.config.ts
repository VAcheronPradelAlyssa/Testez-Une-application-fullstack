import { defineConfig } from 'cypress';

// au lieu de `import…`, utilise require :
const codeCoverageTask = require('@cypress/code-coverage/task');

export default defineConfig({
  e2e: {
    setupNodeEvents(on, config) {
      codeCoverageTask(on, config); // <— appelle directement la fonction renvoyée
      return config;
    },
    baseUrl: 'http://localhost:4200',
    supportFile: 'cypress/support/e2e.ts',
  },
});
