import sys

def changelog(cli_args):
    print("Hello!")
    if len(args) == 0:
        print("Changelog file was not created!")
        sys.exit(1)

    added_files = cli_args[0]
    print("These yaml files were added: ")

    print(added_files)

def check_files_not_modified(cli_args):
    if len(cli_args) == 0:
        print("No files were added/modified")
        return

    all_modified_files, all_added_files = cli_args[0], cli_args[1]

    print("Checking modified files...")
    pass

if __name__ == '__main__':
    args = sys.argv[1:]
    print(f"Running a changelog validation with arguments {args}")

    if args[0] == '-m':
        check_files_not_modified(args[1:])
    if args[0] == '-a':
        changelog(args[1:])


