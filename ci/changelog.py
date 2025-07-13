import sys

def changelog(changed_changelog_files):
    print("Hello!")
    print("These yaml files were added: ")
    print(changed_changelog_files)

def check_files_not_modified(all_changed_files, added_files):
    print("Checking modified files...")
    pass

if __name__ == '__main__':
    print("These arguments were supplied: ")
    print(sys.argv)
    args = sys.argv[1:]
    if args[0] == '-m':
        check_files_not_modified(args[1], args[2])
    if args[0] == '-a':
        changelog(args[1])


