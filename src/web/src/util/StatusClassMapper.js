const StatusClassMapper = {
    statusMap: {
      ORDERED: 'danger',
      PAID: 'info',
      ACQUIRING: 'secondary',
      ACQUIRED: 'primary',
      TRANSIT: 'warning',
      DELIVERED: 'success'
    },
    getClassStatusClass(status) {
      return this.statusMap[status];
    }
  }

  export default StatusClassMapper;
  