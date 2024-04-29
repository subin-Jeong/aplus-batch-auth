create table aplus_point.point_history
(
    id                  bigint auto_increment comment '포인트 이력 고유번호'
        primary key,
    user_id             bigint                                                not null comment '사용자 고유번호',
    affected_history_id bigint                                                null comment '취소한 포인트 사용 이력 고유번호 (포인트 취소 이력이 가지는 상태, 롤백 처리를 위해 저장)',
    initial_point       int                                                   not null comment '포인트 이력 생성 시 포인트 증감분',
    action_type         varchar(20)                                           not null comment '포인트 이력 유형 enum (적립, 사용, 사용 취소)',
    source_type         varchar(50)                                           not null comment '포인트 이력 생성 주체 enum (출석, 제휴 쇼핑, 결제, 기프티콘 등)',
    causes_description  varchar(255)                                          not null comment '포인트 이력 생성 사유 (APP, Web UI에서 노출되는 설명 값)',
    is_cancel           tinyint(1)               default 0                    null comment '포인트 이력 취소 여부',
    create_date         datetime(6)              default current_timestamp(6) not null comment '포인트 이력 생성일',
    update_date         datetime(6)              default current_timestamp(6) not null on update current_timestamp(6) comment '포인트 이력 수정일',
    company             enum ('EST', 'ES', 'EA') default 'EST'                null comment '기록을 발행한 법인명이 기록된다. 적립인 경우 적립처, 발행인 경우 발행처가 기록된다.'
);

create index IDX_POINT_HISTORY_USER_ID_CREATE_AT_ACTION_TYPE
    on aplus_point.point_history (user_id, create_date, action_type);

create table aplus_point.point_accounting_history
(
    id                   bigint auto_increment
        primary key,
    action_type          varchar(20)                            null comment '정산 유형이며 EARN, EXPIRED, REDEEM 중 하나의 값을 가짐',
    source_company       enum ('EST', 'ES', 'EA') default 'EST' not null comment '정산 유형과 관계된 적립처 법인 식별자가 포함된다',
    target_company       enum ('EST', 'ES', 'EA') default 'EST' not null comment '정산 유형과 관계된 사용처 법인 식별자가 포함 그리고 사용과 소멸인 경우 적립처가 포함',
    history_id           bigint                                 not null comment 'point_history 식별자이다.',
    history_date         date                                   not null comment '기록된 날짜를 포함한다.',
    is_canceled          tinyint(1)               default 0     not null comment '해당 포인트 사용 유형이 취소 됐는지 확인한다. 취소된 경우 사용하지 않는 컬럼이 된다. 사용 취소된 경우 사용 취소된 날짜를 기준으로 재정산할 수 있도록 설정한다.',
    related_point_amount int                                    null comment '정산 이력과 관련된 포인트 양을 저장한다.'
)
    comment '포인트 정산 기록';

create index history_date_and_action_type
    on aplus_point.point_accounting_history (history_date desc, action_type asc)
    comment '정산 기록 일자와 정산 유형을 가지고 집계한다.';

create table aplus_point.point_accounting_record
(
    id            bigint auto_increment
        primary key,
    target_date   date                 not null comment '정산한 날짜를 의미한다.',
    is_changed    tinyint(1) default 1 not null comment '정산했을 경우 false, 정산하지 않았을 경우 true를 반환한다. 해당 날짜에 수정이 발생하면 변경 여부가 true가 된다.',
    is_deprecated tinyint(1) default 0 not null comment '사용여부를 의미한다. 5년이 지난 날짜는 더 이상 사용하지 않는다.'
)
    comment '포인트 정산 날짜 기록';

create index is_changed_and_target_date
    on aplus_point.point_accounting_record (is_changed desc, target_date asc)
    comment '변경 여부가 true인 데이터의 변경 일자를 반환한다.';

