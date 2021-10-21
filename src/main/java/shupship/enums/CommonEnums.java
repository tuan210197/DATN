package shupship.enums;

public interface CommonEnums {

	interface DeletedStatus {

		/**
		 * 0. Normal
		 */
		public static Long NORMAL = Long.valueOf(0);

		/**
		 * 9. Deleted
		 */
		public static Long DELETED = Long.valueOf(9);
	}

	interface RestResponseStatus {
		/**
		 * 0. Success
		 */
		public static int SUCCESS = 0;

		/**
		 * 9. Error
		 */
		public static int ERROR = 9;

	}

	interface Status {

		/**
		 * 1. Active
		 */
		public static Long ACTIVE = Long.valueOf(1);

		/**
		 * 9. Inactive
		 */
		public static Long INACTIVE = Long.valueOf(9);
	}

	interface ActionType {

		/**
		 * 1. Insert
		 */
		public static int INSERT = 1;

		/**
		 * 2. Update
		 */
		public static int UPDATE = 2;

		/**
		 * 3. Delete
		 */
		public static int DELETE = 3;

		/**
		 * 4. Convert Lead
		 */
		public static int CONVERT_LEAD = 4;

		/**
		 * 5. Added: Thêm phân tử vào list
		 */
		public static int ADD = 5;

		/**
		 * 6. Removed: Loại phân tử ra khỏi list
		 */
		public static int REMOVE = 6;
	}

	interface LeadConvertStatus {

		/**
		 * 0. Chưa convert, NORMAL
		 */
		public static Long NORMAL = Long.valueOf(0);

		/**
		 * 1. Đã convert
		 */
		public static Long CONVERTED = Long.valueOf(1);

	}

	interface PipelineType {

		/**
		 * 1. Campaign
		 */
		public static String CAMPAIGN = "1";

		/**
		 * 2. Deal
		 */
		public static String DEAL = "2";

	}

	interface ActivityType {

		/**
		 * TASK. Task
		 */
		public static String TASK = "TASK";

		/**
		 * MEETING. Meeting
		 */
		public static String MEETING = "MEETING";

		/**
		 * CALL. Call
		 */
		public static String CALL = "CALL";

	}

	interface RegionLevel {

		/**
		 * PROVINCE. Tỉnh/thành
		 */
		public static String PROVINCE = "PROVINCE";

		/**
		 * DISTRICT. Quận huyện
		 */
		public static String DISTRICT = "DISTRICT";

		/**
		 * WARD. Phường xã
		 */
		public static String WARD = "WARD";

		/**
		 * REGION. khu vực
		 */
		public static String REGION = "REGION";

		/**
		 * COUNTRY. Quốc gia
		 */
		public static String COUNTRY = "COUNTRY";

	}

	interface DataType {

		public static String TEXT = "text";

		public static String DATE = "date";

		public static String NUMBER = "number";

		public static String PICKLIST = "picklist";

	}

	interface LinkType {

		public static String DROPDOWN = "dropdown";

		public static String LOOKUP = "lookup";

	}
	
	interface ModuleType {

		public static String SALE = "SALE";

		public static String AFTER_SALE = "AFTER_SALE";

	}

	/**
	 * Dùng cho workflow
	 * 
	 * @author chiendq
	 *
	 */
	interface ActionCode {

		public static String UPDATE_LEAD = "UPDATE_LEAD";

		public static String CONVERT_LEAD = "CONVERT_LEAD";

		public static String CREATE_TASK = "CREATE_TASK";

	}

	/**
	 * Cách chạy workflow
	 * 
	 * @author chiendq
	 *
	 */
	interface WorkflowExecuteType {

		/**
		 * Khi tạo bản ghi
		 */
		public static String CREATED = "CREATED";

		/**
		 * Khi tạo hoặc update
		 */
		public static String CREATED_UPDATED = "CREATED_UPDATED";

		/**
		 * Hàng ngày
		 */
		public static String EVERY_DAY = "EVERY_DAY";

		/**
		 * Hàng tuần
		 */
		public static String EVERY_WEEK = "EVERY_DAY";

		/**
		 * Hàng tháng
		 */
		public static String EVERY_MONTH = "EVERY_MONTH";

	}

	/**
	 * Cách chạy workflow
	 * 
	 * @author chiendq
	 *
	 */
	interface LeadScore {

		interface RuleScore {

			public static String LEAD = "Lead";

			public static String CONTACT = "Contact";

		}

		interface RuleType {

			public static String PROPERTY = "Property";

			public static String EMAIL = "Email";

		}

		interface RuleAction {

			public static String OPENED = "Opened";

			public static String CLICKED = "Clicked";

		}

		interface Condition {

			public static String EQUAL_TO = "Equal_to";
			public static String NOT_EQUAL_TO = "Not_equal_to";
			public static String GREATER_THAN = "Greater_than";
			public static String GREATER_THAN_OR_EQUAL = "Greater_than_or_equal";
			public static String LESS_THAN = "Less_than";
			public static String LESS_THAN_OR_EQUAL = "Less_than_or_equal";
			public static String IS_EMPTY = "Is_empty";
			public static String IS_NOT_EMPTY = "Is_not_empty";
			public static String IS_IN = "Is_in";
			public static String IS_NOT_IN = "Is_not_in";
			public static String BETWEEN = "Between";
			public static String NOT_BETWEEN = "Not_between";

		}

		interface ChangeType {

			public static String SUB = "Sub";

			public static String ADD = "Add";

		}

	}
	
	interface AttactmentStatus {

		/**
		 * 0. Chờ duyệt <br>
		 * Ở trạng thái này sẽ bị quét rác sau khoảng thời gian nào đó mà không được kích hoạt, các file có thể được move vào thư mục khác
		 * 
		 */
		public static String PENDING = "0";

		/**
		 * 1. Đã duyệt
		 */
		public static String ACTIVE = "1";

		/**
		 * 9. Không có hiệu lực
		 */
		public static String INACTIVE = "9";
	}
}
