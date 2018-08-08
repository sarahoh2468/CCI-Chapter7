public class ParkingLot {
    public enum VehicleSize { Motorcycle, Compact, Large}

    public abstract class Vehicle {
        protected ArrayList<ParkingSpot> parkingSpots = new ArrayLIst<ParkingSpot>();
        protected String licensePLate;
        protected int spotsNeeded;
        protected VehicleSize size;

        public int getSpotsNeeded() {
            return spotsNeeded;
        }
        public VehicleSize getSize() {
            return size;
        }
        public void parkInSpot(ParkingSpot s) {
            parkingSpots.add(spot);
        }
        public void clearSpots() {
            for (int i = 0; i < parkingSpots.size(); i++) {
                parkingSpots.get(i).removeVehicle();
            }
            parkingSpots.clear();
        }
        public abstract boolean canFitInSpot(ParkingSpot spot);
    }

    public class Bus extends Vehicle {
        public Bus() {
            spotsNeeded = 5;
            size = VehicleSize.Large;
        }
        public boolean canFitInSpot(ParkingSpot spot) {
            return spot.getSize() == VehicleSize.Large;
        }
    }
    public class Car extends Vehicle {
        public Car() {
            spotsNeeded = 1;
            size = VehicleSize.Compact;
        }
        public boolean canFitInSpot(ParkingSpot spot) {
            return spot.getSize() == VehicleSize.Large || spot.getSize() == VehicleSize.Compact;
        }
    }
    public class Motorcycle extends Vehicle {
        public Motorcyle() {
            spotsNeeded = 1;
            size = VehicleSize.Motorcycle;
        }

        public boolean canFitInSpot(ParkingSpot spot) {
            return true;
        }
    }

    public class ParkingLot {
        private Level[] levels;
        private final int num_levels = 5;

        public ParkingLot() {
            levels = new Level[num_levels];
            for (int i = 0; i < num_levels; i++) {
                levels[i] = new Level(i, 30);
            }
        }

        public boolean parkVehicle(Vehicle vehicle) {
            for (int i = 0; i < levels.length; i++) {
                if (levels[i].parkVehicle(vehicle)) {
                    return true;
                }
            }
            return false;
        }
    }

    public class Level {
        private int floor;
        private ParkingSpot[] spots;
        private int availableSpots = 0;
        private static final int spots_per_row = 10;

        public Level(int flr, int numberSpots) {
            floor = flr;
            spots = new ParkingSpot[numberSpots];
            int largeSpots = numberSpots / 4;
            int bikeSpots = numberSpots/ 4;
            int compactSpots = numberSpots - largeSpots - bikeSpots;
            for (int i = 0 i < numberSpots; i++) {
                VehicleSize sz = VehicleSize.Motorcycle;
                if (i < largeSpots) {
                    sz = VehicleSize.Large;
                }
                else if (i < largeSpots + compactSpots) {
                    sz = VehicleSize.Compact;
                }
                int row = i / spots_per_row;
                spots[i] = new ParkingSpot(this, row, i, sz);
            }
            availableSpots = numberSpots;
        }
        public int availableSpots() {
            return availableSpots;
        }

        public boolean parkVehicle(Vehicle vehicle) {
            if (availableSpots() < vehicle.getSpotsNeeded()) {
                return false;
            }
            int spotNumber = findAvailableSpots(vehicle);
            if (spotNumber < 0) {
                return false;
            }
            return parkStartingAtSpot(spotNumber, vehicle);
        }

        public boolean parkStartingAtSpot(int spotNumber, Vehicle vehicle) {
            vehicle.clearSpots();
            boolean success = true;
            for (int i = spotNumber; i < spotNumber + vehicle.spotsNeeded; i++) {
                success &= spots[i].park(vehicle);
            }
            availableSpots -= vehicle.spotsNeeded;
            return success;
        }
        private int findAvailableSpots(Vehicle vehicle) {
            int spotsNeeded = vehicle.getSpotsNeeded();
            int lastRow = -1;
            int spotsFound = 0;
            for (int i = 0; i < spots.length; i++) {
                ParkingSpot spot = spots[i];
                if (lastRow != spot.getRow()) {
                    spotsFound = 0;
                    lastRow = spot.getRow();
                }
                if (spot.canFitVehicle(vehicle)) {
                    spotsFound++;
                }
                else {
                    spotsFound = 0;
                }
                if (spotsFound == spotsNeeded) {
                    return i - (spotsNeeded - 1);
                }
            }
            return -1;
        }
        public void spotFreed() {
            availableSpots++;
        }
    }
    public class ParkingSpot {
        private Vehicle vehicle;
        private VehicleSize spotSize;
        private int row;
        private int spotNumber;
        private Level level;

        public ParkingSpot(Level lvl, int r, int n, VehicleSize s) {
            level = lvl;
            row = r;
            spotNumber = n;
            spotSize = s;
        }

        public boolean isAvailable() {
            return vehicle == null;
        }

        public boolean canFitVehicle(Vehicle vehicle) {
            return isAvailable() && vehicle.canFitInSpot(this);
        }

        public boolean park(Vehicle v) {
            if (!canFitVehicle()) {
                return false;
            }
            vehicle = v;
            vehicle.parkInSpot(this);
            return true;
        }

        public int getRow() {
            return row;
        }

        public int getSpotNumber() {
            return spotNumber;
        }

        public VehicleSize getSize() {
            return spotSize;
        }

        public void removeVehicle() {
            level.spotFreed();
            vehicle = null;
        }
    }
}
