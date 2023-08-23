package org.eclipse.trace.coordinator.core.style;

public enum LinuxStyle {
	IDLE {
		@Override
		public String toString() {
			return "Idle";
		}
	},
	INTERRUPT {
		@Override
		public String toString() {
			return "Interrupt";
		}
	},
	CPU_TRANSITION {
		@Override
		public String toString() {
			return "CPU transition";
		}
	},
	SOFT_IRQ_RAISED {
		@Override
		public String toString() {
			return "Soft Irq raised";
		}
	},
	SOFT_IRQ {
		@Override
		public String toString() {
			return "Soft Irq";
		}
	},
	SYSTEM_CALL {
		@Override
		public String toString() {
			return "System call";
		}
	},
	UNKNOW {
		@Override
		public String toString() {
			return "Unknown";
		}
	},
	USERMODE {
		@Override
		public String toString() {
			return "Usermode";
		}
	},
	WAIT {
		@Override
		public String toString() {
			return "Wait";
		}
	},
	WAIT_BLOCKED {
		@Override
		public String toString() {
			return "Wait blocked";
		}
	},
	WAIT_FOR_CPU {
		@Override
		public String toString() {
			return "Wait for CPU";
		}
	},
	PROCESS_GROUP {
		@Override
		public String toString() {
			return "Process States";
		}
	},
	LINK_GROUP {
		@Override
		public String toString() {
			return "Arrows";
		}
	}
}
