const Future = require('../../../src/future')

const client = new Future()


client.ContractDetail().then(response => client.logger.log(response.data))
  .catch(error => client.logger.error(error))