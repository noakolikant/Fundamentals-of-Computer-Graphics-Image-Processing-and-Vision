function [ inliers, M ] = homdistfn(M, x, t)
%distfn 
    error = 0;
    inliers = [];
    for i = 1:size(x, 2)
        hom_point = M * x(1:3, i);
        % Take only the Hom. parts
        distance = ((x(4, i) - hom_point(1)).^2 + ...
            (x(5 ,i) - hom_point(2)).^2);
        if distance < t
            error = error + distance;
            inliers = [inliers; i];
        end
    end


end

