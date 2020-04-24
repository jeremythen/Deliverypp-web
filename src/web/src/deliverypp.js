
const ENVIRONMENTS = {
    DEV: 'dev',
    QA: 'qa',
    PROD: 'prod'
};

const STATUS = {
    SUCCESS: 'SUCCESS',
    ERROR: 'ERROR'
};

const deliverypp = {
    env: ENVIRONMENTS.DEV,
    getPath() {
        switch(this.env) {
            case ENVIRONMENTS.DEV:
                return 'http://localhost:8080';
            case ENVIRONMENTS.QA:
            case ENVIRONMENTS.PROD:
                return '/'
        }
    },
    STATUS
};

export default  deliverypp;