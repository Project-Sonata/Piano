import sys

import yaml

def changelog(cli_args):
    if len(args) == 0:
        print("Failure! Changelog file was not created!")
        sys.exit(1)

    added_files = cli_args[0].split("\n")

    if len(added_files) != 1:
        print("Failure! Multiple changelog files were created!")
        sys.exit(1)

    with open(f"changelog/{added_files[0]}") as stream:
        try:
            changelog_file_content = yaml.safe_load(stream)
            if changelog_file_content is None:
                print("Failure! Changelog cannot be empty!")
                sys.exit(1)

            if changelog_file_content['sonata'] is None:
                print("Failure! Changelog should start with 'sonata'")
                sys.exit(1)

            if changelog_file_content['sonata']['piano'] is None:
                print("Failure! Changelog should start with 'sonata.piano'")
                sys.exit(1)

            if changelog_file_content['sonata']['piano']['enhancement'] is None:
                print("Failure! Changelog should start with 'sonata.piano.enhancement'")
                sys.exit(1)

            messages = changelog_file_content['sonata']['piano']['enhancement']

            for message in messages:
                message_parts = message.split(":")
                if len(message_parts) == 1:
                    print("Failure! Message of the changelog should contain ticket name and description of the change!")
                    sys.exit(1)

                ticket_number, ticket_desc = message_parts[0], message_parts[1]

                if ticket_number.strip() == '':
                    print("Failure! Ticket number is whitespaces only")
                    sys.exit(1)
                if ticket_desc.strip() == '':
                    print("Failure! Ticket description is whitespaces only")
                    sys.exit(1)

            print("Success! Changelog is valid!")
        except yaml.YAMLError as exc:
            print(exc)
            sys.exit(1)


def check_files_not_modified(cli_args):
    if len(cli_args) == 0:
        print("No files were added/modified")
        return

    all_modified_files, all_added_files = cli_args[0], cli_args[1]

    print("Checking modified files...")

    if all_modified_files != all_added_files:
        print("Failure! Files were modified")
        sys.exit(1)
    print("Success! No files were modified.")

if __name__ == '__main__':
    args = sys.argv[1:]
    print(f"Running a changelog validation with arguments {args}")

    if args[0] == '-m':
        check_files_not_modified(args[1:])
    if args[0] == '-a':
        changelog(args[1:])


