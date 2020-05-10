
const ENVIRONMENTS = {
    getActiveEnv() {
        return Deliverypp;
    },
    DEV: 'dev',
    QA: 'qa',
    PROD: 'prod'
};

const STATUS = {
    SUCCESS: 'SUCCESS',
    ERROR: 'ERROR'
};

const Deliverypp = {
    env: ENVIRONMENTS.DEV,
    getPath() {
        switch(this.env) {
            case ENVIRONMENTS.DEV:
                return '';
            case ENVIRONMENTS.QA:
            case ENVIRONMENTS.PROD:
                return ''
            default:
                console.error('Wrong operation.');
                break;
        }
    },
    STATUS
};

export default  Deliverypp;