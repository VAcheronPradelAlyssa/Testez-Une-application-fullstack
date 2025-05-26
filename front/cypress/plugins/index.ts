import * as codeCoverageTasks from '@cypress/code-coverage/task';

const pluginConfig: Cypress.PluginConfig = (on, config) => {
  codeCoverageTasks.registerCodeCoverageTasks(on, config);
  return config;
};

export default pluginConfig;
