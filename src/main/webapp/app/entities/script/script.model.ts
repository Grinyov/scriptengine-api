import { BaseEntity, User } from './../../shared';

const enum Status {
    'NEW',
    ' RUNNING',
    ' DONE',
    ' FAILED'
}

export class Script implements BaseEntity {
    constructor(
        public id?: number,
        public script?: string,
        public status?: Status,
        public result?: string,
        public user?: User,
    ) {
    }
}
