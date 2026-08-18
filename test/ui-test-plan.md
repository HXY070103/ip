# Console UI Test Plan

Run the cases with:

```bash
python3 .codex/skills/test-ui/scripts/run_ui_tests.py --plan test/ui-test-plan.md
```

Each test case starts a fresh `Tianyi` process. Inputs within one case run in order and share the same in-memory task list. Output comparison is exact except that CRLF and CR line endings are normalized to LF. Testing stops at the first failed case.

```json
{
  "main_class": "Tianyi",
  "timeout_seconds": 10,
  "test_cases": [
    {
      "id": "todo-and-list",
      "aim": "Verify that a todo task is added, counted, and shown by list.",
      "inputs": [
        "todo borrow book",
        "list",
        "bye"
      ],
      "expected_output": [
        "____________________________________________________________",
        " _____ _                   _",
        "|_   _(_) __ _ _ __  _   _(_)",
        "  | | | |/ _` | '_ \\| | | | |",
        "  | | | | (_| | | | | |_| | |",
        "  |_| |_|\\__,_|_| |_|\\__, |_|",
        "                     |___/",
        "Hello! I'm Tianyi.",
        "What can I do for you?",
        "____________________________________________________________",
        "____________________________________________________________",
        "Got it. I've added this task:",
        "  [T][ ] borrow book",
        "Now you have 1 tasks in the list.",
        "____________________________________________________________",
        "____________________________________________________________",
        "Here are the tasks in your list:",
        "1.[T][ ] borrow book",
        "____________________________________________________________",
        "____________________________________________________________",
        "Bye. Hope to see you again soon!",
        "____________________________________________________________"
      ]
    },
    {
      "id": "deadline-with-time",
      "aim": "Verify that /by separates a deadline description from its time.",
      "inputs": [
        "deadline return book /by Sunday",
        "bye"
      ],
      "expected_output": [
        "____________________________________________________________",
        " _____ _                   _",
        "|_   _(_) __ _ _ __  _   _(_)",
        "  | | | |/ _` | '_ \\| | | | |",
        "  | | | | (_| | | | | |_| | |",
        "  |_| |_|\\__,_|_| |_|\\__, |_|",
        "                     |___/",
        "Hello! I'm Tianyi.",
        "What can I do for you?",
        "____________________________________________________________",
        "____________________________________________________________",
        "Got it. I've added this task:",
        "  [D][ ] return book (by: Sunday)",
        "Now you have 1 tasks in the list.",
        "____________________________________________________________",
        "____________________________________________________________",
        "Bye. Hope to see you again soon!",
        "____________________________________________________________"
      ]
    },
    {
      "id": "event-with-time-range",
      "aim": "Verify that /from and /to separate an event description and time range.",
      "inputs": [
        "event project meeting /from Mon 2pm /to 4pm",
        "bye"
      ],
      "expected_output": [
        "____________________________________________________________",
        " _____ _                   _",
        "|_   _(_) __ _ _ __  _   _(_)",
        "  | | | |/ _` | '_ \\| | | | |",
        "  | | | | (_| | | | | |_| | |",
        "  |_| |_|\\__,_|_| |_|\\__, |_|",
        "                     |___/",
        "Hello! I'm Tianyi.",
        "What can I do for you?",
        "____________________________________________________________",
        "____________________________________________________________",
        "Got it. I've added this task:",
        "  [E][ ] project meeting (from: Mon 2pm to: 4pm)",
        "Now you have 1 tasks in the list.",
        "____________________________________________________________",
        "____________________________________________________________",
        "Bye. Hope to see you again soon!",
        "____________________________________________________________"
      ]
    }
  ]
}
```
