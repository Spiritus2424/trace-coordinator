package org.eclipse.trace.coordinator.core.style;

public enum CriticalPathStyle {
	/**
	 * Special type of edge meaning there is no edge, to differentiate
	 * with null
	 * edges at the beginning and end of workers
	 */
	NO_EDGE {
		@Override
		public String toString() {
			return "Unknown";
		}
	},
	/**
	 * Special edge, so it is possible to have two vertices at the same timestamp.
	 */
	EPS {
		@Override
		public String toString() {
			return "Unknown";
		}
	},
	/** Unknown edge */
	UNKNOWN {
		@Override
		public String toString() {
			return "Unknown";
		}
	},
	/** Default type for an edge */
	DEFAULT {
		@Override
		public String toString() {
			return "Unknown";
		}
	},
	/** Worker is running */
	RUNNING {
		@Override
		public String toString() {
			return "Running";
		}
	},
	/** Worker is blocked */
	BLOCKED {
		@Override
		public String toString() {
			return "Unknown Blocked";
		}
	},
	/** Worker is in an interrupt state */
	INTERRUPTED {
		@Override
		public String toString() {
			return "Interrupted";
		}
	},
	/** Worker is preempted */
	PREEMPTED {
		@Override
		public String toString() {
			return "Preempted";
		}
	},
	/** In a timer */
	TIMER {
		@Override
		public String toString() {
			return "Timer";
		}
	},
	/** Edge represents a network communication */
	NETWORK {
		@Override
		public String toString() {
			return "Network";
		}
	},
	/** Worker is waiting for user input */
	USER_INPUT {
		@Override
		public String toString() {
			return "User Input";
		}
	},
	/** Block device */
	BLOCK_DEVICE {
		@Override
		public String toString() {
			return "Block Device";
		}
	},
	/** inter-processor interrupt */
	IPI {
		@Override
		public String toString() {
			return "IPI";
		}
	},
}
